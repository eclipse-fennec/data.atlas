/**
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 */
package org.eclipse.fennec.data.atlas.transformation;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.data.atlas.api.DataTransformer;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;

/**
 * {@link DataTransformer} over a QVT-O AST: every {@link #transform(List)}
 * executes the transformation with the source objects as the (read-only) in
 * extent and returns the contents of the out extent.
 *
 * <p>
 * The AST is parsed/loaded once and reused across executions; execution is
 * serialized on this instance because one engine's concurrent execution
 * behaviour is not part of its contract. The Data Atlas 1:1 contract is
 * enforced: an execution that does not produce exactly one result object per
 * source object fails.
 * </p>
 */
public class QvtoDataTransformer implements DataTransformer {

	private final String id;
	private final EClass inputType;
	private final EClass outputType;
	private final OperationalTransformation ast;
	private final QvtoEngine engine;

	public QvtoDataTransformer(String id, EClass inputType, EClass outputType, OperationalTransformation ast,
			QvtoEngine engine) {
		this.id = id;
		this.inputType = inputType;
		this.outputType = outputType;
		this.ast = ast;
		this.engine = engine;
	}

	public String id() {
		return id;
	}

	@Override
	public EClass inputType() {
		return inputType;
	}

	@Override
	public EClass outputType() {
		return outputType;
	}

	@Override
	public synchronized List<EObject> transform(List<? extends EObject> sourceObjects) {
		if (sourceObjects.isEmpty()) {
			return List.of();
		}
		BasicQvtoModelExtent in = new BasicQvtoModelExtent(sourceObjects);
		in.setReadOnly(true);
		BasicQvtoModelExtent out = new BasicQvtoModelExtent();
		QvtoExecutionResult result;
		try {
			result = engine.execute(ast, QvtoExecutionContext.of(in, out));
		} catch (RuntimeException e) {
			throw new TransformationException("transformation '" + id + "' failed: " + e.getMessage(), e);
		}
		if (!result.isSuccess()) {
			throw new TransformationException("transformation '" + id + "' failed: " + describe(result));
		}
		List<EObject> outputs = out.getContents();
		if (outputs.size() != sourceObjects.size()) {
			throw new TransformationException("transformation '" + id + "' violates the 1:1 contract: "
					+ sourceObjects.size() + " source object(s) produced " + outputs.size() + " result object(s)");
		}
		return List.copyOf(outputs);
	}

	private String describe(QvtoExecutionResult result) {
		StringBuilder text = new StringBuilder();
		for (Diagnostic diagnostic : result.diagnostics()) {
			if (diagnostic.getSeverity() >= Diagnostic.ERROR) {
				if (text.length() > 0) {
					text.append("; ");
				}
				text.append(diagnostic.getMessage());
			}
		}
		return text.length() == 0 ? "unspecified engine error" : text.toString();
	}
}
