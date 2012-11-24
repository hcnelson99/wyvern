package wyvern.tools.typedAST;

import wyvern.tools.parsing.LineParser;
import wyvern.tools.parsing.LineSequenceParser;
import wyvern.tools.util.AbstractTreeWritable;

public abstract class AbstractTypedAST extends AbstractTreeWritable implements TypedAST {
	/** may return null */
	@Override
	public LineParser getLineParser() {
		return null;
	}

	/** may return null */
	public LineSequenceParser getLineSequenceParser() {
		return null;
	}
}
