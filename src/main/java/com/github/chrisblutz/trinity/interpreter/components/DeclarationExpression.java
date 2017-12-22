package com.github.chrisblutz.trinity.interpreter.components;

import com.github.chrisblutz.trinity.interpreter.Interpreter;
import com.github.chrisblutz.trinity.interpreter.Location;
import com.github.chrisblutz.trinity.interpreter.instructions.InstructionSet;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;


/**
 * @author Christopher Lutz
 */
public abstract class DeclarationExpression {
    
    public abstract InstructionSet interpret(SourceToken[] line, Block next, Interpreter interpreter, Location location);
}
