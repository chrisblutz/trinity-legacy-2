package com.github.chrisblutz.trinity.parser;

import com.github.chrisblutz.trinity.lang.errors.Errors;
import com.github.chrisblutz.trinity.parser.blocks.Block;
import com.github.chrisblutz.trinity.parser.blocks.BlockItem;
import com.github.chrisblutz.trinity.parser.blocks.Statement;
import com.github.chrisblutz.trinity.parser.lines.Line;
import com.github.chrisblutz.trinity.parser.lines.LineSet;
import com.github.chrisblutz.trinity.parser.tokens.SourceToken;
import com.github.chrisblutz.trinity.parser.tokens.Token;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Christopher Lutz
 */
public class Parser {
    
    public static final String DECIMAL_REGEX = "[0-9]+([lLfF]|\\.[0-9]+[fF]?)?";
    public static final String HEXADECIMAL_REGEX = "0x[a-fA-F0-9]+([lL]|\\.[a-fA-F0-9]+)?";
    public static final String OCTAL_REGEX = "0o[0-7]+([lLfF]|\\.[0-7]+[fF]?)?";
    public static final String BINARY_REGEX = "0b[0-1]+([lLfF]|\\.[0-1]+[fF]?)?";
    
    private SourceEntry sourceEntry;
    
    public Parser(SourceEntry sourceEntry) {
        
        this.sourceEntry = sourceEntry;
    }
    
    public SourceEntry getSourceEntry() {
        
        return sourceEntry;
    }
    
    public Block parse() {
        
        LineSet set = parseTokens();
        return parseToBlock(set);
    }
    
    public LineSet parseTokens() {
        
        LineSet set = parseInitial();
        set = parseOutEscapeCharacters(set);
        set = parseOutUnicodeEscapes(set);
        set = parseSpecialTokenStrings(set);
        set = parseLiteralStrings(set);
        set = parseOutComments(set);
        set = parseOutSpaces(set);
        set = parseOutLeadingWhitespace(set);
        set = parseNumericStrings(set);
        set = parseOutNativeMethodDefinitions(set);
        set = parseScopes(set);
        set = parseOutEmptyLines(set);
        
        return set;
    }
    
    private LineSet parseInitial() {
        
        LineSet set = new LineSet(getSourceEntry());
        
        int litStrIndex = 0, tokenIndex = 0;
        String litStr = "", token = "";
        
        String[] lines = getSourceEntry().getLines();
        
        for (int i = 0; i < lines.length; i++) {
            
            String lineStr = lines[i];
            Line line = new Line(getSourceEntry().getStartingLine() + i);
            
            for (int j = 0; j < lineStr.length(); j++) {
                
                String tokenString = Character.toString(lineStr.charAt(j));
                
                if (Token.isToken(tokenString)) {
                    
                    if (!litStr.isEmpty()) {
                        
                        if (Token.isToken(litStr)) {
                            
                            line.add(constructSourceToken(litStr, line.getLineNumber(), litStrIndex));
                            
                        } else {
                            
                            line.add(new SourceToken(Token.TOKEN_STRING, line.getLineNumber(), litStrIndex, litStr, getSourceEntry()));
                        }
                        
                        litStr = "";
                    }
                    
                    if (!token.isEmpty() && Token.isToken(token)) {
                        
                        if (Token.isToken(token + tokenString)) {
                            
                            token += tokenString;
                            
                        } else {
                            
                            line.add(constructSourceToken(token, line.getLineNumber(), tokenIndex));
                            token = tokenString;
                            tokenIndex = j;
                        }
                        
                    } else {
                        
                        if (token.isEmpty()) {
                            
                            tokenIndex = j;
                        }
                        
                        token += tokenString;
                    }
                    
                } else {
                    
                    if (!token.isEmpty()) {
                        
                        line.add(constructSourceToken(token, line.getLineNumber(), tokenIndex));
                        token = "";
                    }
                    
                    if (litStr.isEmpty()) {
                        
                        litStrIndex = j;
                    }
                    
                    litStr += tokenString;
                }
            }
            
            if (!token.isEmpty() && Token.isToken(token)) {
                
                line.add(constructSourceToken(token, line.getLineNumber(), tokenIndex));
                token = "";
                
            } else if (!litStr.isEmpty()) {
                
                if (Token.isToken(litStr)) {
                    
                    line.add(constructSourceToken(litStr, line.getLineNumber(), litStrIndex));
                    
                } else {
                    
                    line.add(new SourceToken(Token.TOKEN_STRING, line.getLineNumber(), litStrIndex, litStr, getSourceEntry()));
                }
                
                litStr = "";
            }
            
            set.add(line);
        }
        
        return set;
    }
    
    private LineSet parseOutEscapeCharacters(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            for (int i = 0; i < line.size(); i++) {
                
                SourceToken token = line.get(i);
                
                if (token.getToken() == Token.BACKSLASH && i + 1 < line.size() && line.get(i + 1).getToken() == Token.TOKEN_STRING) {
                    
                    SourceToken next = line.get(++i);
                    String escape = next.getContents();
                    
                    String escapeSeq = "\\" + escape.charAt(0);
                    if (Token.isToken(escapeSeq)) {
                        
                        newLine.add(constructSourceToken(escapeSeq, line.getLineNumber(), token.getIndex()));
                        
                        String remaining = escape.substring(1);
                        if (!remaining.isEmpty()) {
                            
                            newLine.add(new SourceToken(Token.TOKEN_STRING, line.getLineNumber(), next.getIndex() + 1, remaining, getSourceEntry()));
                        }
                        
                    } else {
                        
                        newLine.add(token);
                        newLine.add(next);
                    }
                    
                } else {
                    
                    newLine.add(token);
                }
            }
            
            newSet.add(newLine);
        }
        
        return newSet;
    }
    
    private LineSet parseOutUnicodeEscapes(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            for (int i = 0; i < line.size(); i++) {
                
                SourceToken token = line.get(i);
                
                if (token.getToken() == Token.UNICODE_ESCAPE) {
                    
                    String sequence = "", remaining = "";
                    int remainingIndex = -1;
                    if (i + 1 < line.size() && line.get(i + 1).getToken() == Token.TOKEN_STRING) {
                        
                        String full = line.get(++i).getContents();
                        int seqLength = Math.min(full.length(), 4);
                        sequence = full.substring(0, seqLength);
                        remaining = full.substring(seqLength);
                        remainingIndex = line.get(i).getIndex() + 4;
                        
                    } else if (i + 3 < line.size() && line.get(i + 1).getToken() == Token.LEFT_CURLY_BRACKET && line.get(i + 2).getToken() == Token.TOKEN_STRING && line.get(i + 3).getToken() == Token.RIGHT_CURLY_BRACKET) {
                        
                        sequence = line.get(i + 2).getContents();
                        i += 3;
                        remainingIndex = line.get(i).getIndex() + 1;
                    }
                    
                    newLine.add(new SourceToken(Token.UNICODE_ESCAPE, line.getLineNumber(), token.getIndex(), sequence, getSourceEntry()));
                    
                    if (!remaining.isEmpty()) {
                        
                        newLine.add(new SourceToken(Token.TOKEN_STRING, line.getLineNumber(), remainingIndex, remaining, getSourceEntry()));
                    }
                    
                } else {
                    
                    newLine.add(token);
                }
            }
            
            newSet.add(newLine);
        }
        
        return newSet;
    }
    
    private LineSet parseSpecialTokenStrings(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            for (int i = 0; i < line.size(); i++) {
                
                SourceToken token = line.get(i);
                
                if (token.getToken() == Token.TOKEN_STRING) {
                    
                    if (token.getContents().equals("block") && i + 1 < line.size() && line.get(i + 1).getToken() == Token.QUESTION_MARK) {
                        
                        newLine.add(new SourceToken(Token.BLOCK_CHECK, line.getLineNumber(), token.getIndex(), Token.BLOCK_CHECK.getReadable(), getSourceEntry()));
                        i++;
                        
                    } else {
                        
                        newLine.add(token);
                    }
                    
                } else {
                    
                    newLine.add(token);
                }
            }
            
            newSet.add(newLine);
        }
        
        return newSet;
    }
    
    private LineSet parseLiteralStrings(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            int currentIndex = -1, unescapedEmbedLevel = 0;
            boolean inEsc = false, inUnescaped = false, inEmbed = false;
            StringBuilder current = new StringBuilder();
            
            for (SourceToken token : line) {
                
                if (inEsc) {
                    
                    if (token.getToken() == Token.SINGLE_QUOTATION) {
                        
                        inEsc = false;
                        newLine.add(new SourceToken(Token.LITERAL_STRING, line.getLineNumber(), currentIndex, 1 + current.toString(), getSourceEntry()));
                        current = new StringBuilder();
                        
                    } else {
                        
                        current.append(getAppendableString(token, true));
                    }
                    
                } else if (inUnescaped) {
                    
                    if (token.getToken() == Token.DOUBLE_QUOTATION && !inEmbed) {
                        
                        inUnescaped = false;
                        newLine.add(new SourceToken(Token.LITERAL_STRING, line.getLineNumber(), currentIndex, 0 + current.toString(), getSourceEntry()));
                        current = new StringBuilder();
                        
                    } else {
                        
                        if (!inEmbed && token.getToken() == Token.STRING_EMBED_PREFIX) {
                            
                            inEmbed = true;
                            unescapedEmbedLevel++;
                            
                        } else if (inEmbed) {
                            
                            if (token.getToken() == Token.LEFT_CURLY_BRACKET || token.getToken() == Token.STRING_EMBED_PREFIX) {
                                
                                unescapedEmbedLevel++;
                                
                            } else if (token.getToken() == Token.RIGHT_CURLY_BRACKET) {
                                
                                unescapedEmbedLevel--;
                                
                                if (unescapedEmbedLevel == 0) {
                                    
                                    inEmbed = false;
                                }
                            }
                        }
                        
                        current.append(getAppendableString(token, false));
                    }
                    
                } else {
                    
                    if (token.getToken() == Token.SINGLE_QUOTATION) {
                        
                        currentIndex = token.getIndex();
                        inEsc = true;
                        
                    } else if (token.getToken() == Token.DOUBLE_QUOTATION) {
                        
                        currentIndex = token.getIndex();
                        unescapedEmbedLevel = 0;
                        inUnescaped = true;
                        
                    } else {
                        
                        newLine.add(token);
                    }
                }
            }
            
            if (inEsc || inUnescaped) {
                
                Errors.reportSyntaxError("String does not terminate.", getSourceEntry(), line.getLineNumber(), currentIndex);
            }
            
            newSet.add(newLine);
        }
        
        return newSet;
    }
    
    private String getAppendableString(SourceToken token, boolean escape) {
        
        if (token.getToken() == Token.TOKEN_STRING) {
            
            return token.getContents();
            
        } else if (escape && !(token.getToken() == Token.BACKSLASH_ESCAPE || token.getToken() == Token.SINGLE_QUOTE_ESCAPE || token.getToken() == Token.UNICODE_ESCAPE)) {
            
            return token.getToken().getReadable();
            
        } else if (token.getToken() == Token.UNICODE_ESCAPE) {
            
            return getUnicodeCharSequence(token.getContents());
            
        } else {
            
            return token.getToken().getLiteral();
        }
    }
    
    private String getUnicodeCharSequence(String sequence) {
        
        int codePoint = Integer.parseInt(sequence, 16);
        return new StringBuilder().appendCodePoint(codePoint).toString();
    }
    
    private LineSet parseOutComments(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            for (SourceToken token : line) {
                
                if (token.getToken() == Token.COMMENT_MARKER || token.getToken() == Token.STRING_EMBED_PREFIX) {
                    
                    break;
                    
                } else {
                    
                    newLine.add(token);
                }
            }
            
            newSet.add(newLine);
        }
        
        return newSet;
    }
    
    private LineSet parseOutSpaces(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            boolean inLeading = true;
            for (SourceToken token : line) {
                
                if (inLeading && token.getToken() != Token.WS_SPACE && token.getToken() != Token.WS_TAB) {
                    
                    inLeading = false;
                }
                
                if (inLeading || (token.getToken() != Token.WS_SPACE && token.getToken() != Token.WS_TAB)) {
                    
                    newLine.add(token);
                }
            }
            
            newSet.add(newLine);
        }
        
        return newSet;
    }
    
    private LineSet parseOutLeadingWhitespace(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        boolean hasSpaces = false, hasTabs = false;
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            boolean inLeading = true;
            int leadingSpace = 0;
            
            for (SourceToken token : line) {
                
                if (inLeading) {
                    
                    if (token.getToken() == Token.WS_SPACE) {
                        
                        hasSpaces = true;
                        leadingSpace++;
                        
                    } else if (token.getToken() == Token.WS_TAB) {
                        
                        hasTabs = true;
                        leadingSpace++;
                        
                    } else {
                        
                        inLeading = false;
                        newLine.add(token);
                    }
                    
                } else {
                    
                    newLine.add(token);
                }
            }
            
            newLine.setLeadingSpace(leadingSpace);
            newSet.add(newLine);
        }
        
        if (hasSpaces && hasTabs) {
            
            Errors.reportSyntaxError("Files cannot contain tabs and spaces.", getSourceEntry());
        }
        
        return newSet;
    }
    
    private LineSet parseNumericStrings(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            for (int i = 0; i < line.size(); i++) {
                
                SourceToken token = line.get(i);
                
                if (token.getToken() == Token.TOKEN_STRING) {
                    
                    if (i + 2 < line.size() && line.get(i + 1).getToken() == Token.DOT_OP && line.get(i + 2).getToken() == Token.TOKEN_STRING && isNumericString(token.getContents() + "." + line.get(i + 2).getContents())) {
                        
                        newLine.add(new SourceToken(Token.NUMERIC_STRING, line.getLineNumber(), token.getIndex(), token.getContents() + "." + line.get(i + 2).getContents(), getSourceEntry()));
                        i += 2;
                        
                    } else if (isNumericString(line.get(i).getContents())) {
                        
                        newLine.add(new SourceToken(Token.NUMERIC_STRING, line.getLineNumber(), token.getIndex(), token.getContents(), getSourceEntry()));
                        
                    } else {
                        
                        newLine.add(token);
                    }
                    
                } else {
                    
                    newLine.add(token);
                }
            }
            
            newSet.add(newLine);
        }
        
        return newSet;
    }
    
    private boolean isNumericString(String s) {
        
        return s.matches(DECIMAL_REGEX) || s.matches(HEXADECIMAL_REGEX) || s.matches(OCTAL_REGEX) || s.matches(BINARY_REGEX);
    }
    
    private LineSet parseOutNativeMethodDefinitions(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            if (line.size() > 1) {
                
                Line newLine = new Line(line.getLineNumber());
                
                int i = 0;
                SourceToken token = line.get(i++);
                
                if (token.getToken() == Token.DEF && line.get(i).getToken() == Token.NATIVE) {
                    
                    i++;
                    newLine.add(new SourceToken(Token.DEF_NATIVE, line.getLineNumber(), token.getIndex(), Token.DEF_NATIVE.getReadable(), getSourceEntry()));
                    
                } else {
                    
                    newLine.add(token);
                }
                
                for (; i < line.size(); i++) {
                    
                    newLine.add(line.get(i));
                }
                
                newSet.add(newLine);
                
            } else {
                
                newSet.add(line);
            }
        }
        
        return newSet;
    }
    
    private LineSet parseScopes(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            Line newLine = new Line(line.getLineNumber());
            
            for (int i = 0; i < line.size(); i++) {
                
                SourceToken token = line.get(i);
                
                if (i + 2 < line.size() && token.getToken() == Token.MODULE && line.get(i + 1).getToken() == Token.MINUS_OP && line.get(i + 2).getToken() == Token.PROTECTED) {
                    
                    newLine.add(new SourceToken(Token.SCOPE_MODIFIER, line.getLineNumber(), token.getIndex(), "module-protected", getSourceEntry()));
                    i += 2;
                    
                } else if (token.getToken() == Token.PUBLIC || token.getToken() == Token.PROTECTED || token.getToken() == Token.PRIVATE) {
                    
                    newLine.add(new SourceToken(Token.SCOPE_MODIFIER, line.getLineNumber(), token.getIndex(), token.getContents(), getSourceEntry()));
                    
                } else {
                    
                    newLine.add(token);
                }
            }
            
            newSet.add(newLine);
        }
        
        return newSet;
    }
    
    private LineSet parseOutEmptyLines(LineSet set) {
        
        LineSet newSet = new LineSet(getSourceEntry());
        
        for (Line line : set) {
            
            if (!line.isEmpty()) {
                
                newSet.add(line);
            }
        }
        
        return newSet;
    }
    
    private SourceToken constructSourceToken(String token, int line, int index) {
        
        return new SourceToken(Token.forString(token), line, index, token, getSourceEntry());
    }
    
    private Block parseToBlock(LineSet lines) {
        
        Block block = new Block(getSourceEntry());
        
        block.addAll(parseBlock(lines));
        
        return block;
    }
    
    private List<BlockItem> parseBlock(List<Line> lines) {
        
        List<BlockItem> items = new ArrayList<>();
        
        for (int i = 0; i < lines.size(); i++) {
            
            Line line = lines.get(i);
            
            if (line.get(0).getToken().isBlocking()) {
                
                Statement statement = new Statement(getSourceEntry(), line.getLineNumber());
                statement.addAll(line);
                
                Block following = new Block(getSourceEntry());
                int blockLevel = 0;
                
                List<Line> subLines = new ArrayList<>();
                int j = i + 1;
                while (true) {
                    
                    if (j < lines.size()) {
                        
                        Line subLine = lines.remove(j);
                        if (subLine.get(0).getToken().isBlockContinuing()) {
                            
                            if (blockLevel == 0) {
                                
                                lines.add(j, subLine);
                                break;
                            }
                            
                        } else if (subLine.get(0).getToken().isBlocking() || subLine.containsToken(Token.DO)) {
                            
                            blockLevel++;
                            
                        } else if (subLine.get(0).getToken() == Token.END) {
                            
                            if (blockLevel == 0) {
                                
                                break;
                                
                            } else {
                                
                                blockLevel--;
                            }
                        }
                        
                        subLines.add(subLine);
                        
                    } else {
                        
                        Errors.reportSyntaxError("Block does not terminate.", getSourceEntry(), line.getLineNumber(), line.get(0).getIndex());
                        break;
                    }
                }
                
                following.addAll(parseBlock(subLines));
                
                items.add(statement);
                
                if (!following.isEmpty()) {
                    
                    items.add(following);
                }
                
            } else if (line.containsToken(Token.DO)) {
                
                int doIndex = 0;
                List<SourceToken> blockHeader = new ArrayList<>();
                if (line.get(line.size() - 1).getToken() == Token.BITWISE_OR_OP) {
                    
                    int j;
                    for (j = line.size() - 2; j >= 0; j--) {
                        
                        if (line.get(j).getToken() == Token.DO) {
                            
                            doIndex = line.get(j).getIndex();
                            break;
                        }
                    }
                    
                    line.remove(j);
                    line.remove(j);
                    while (j < line.size() - 1) {
                        
                        blockHeader.add(line.remove(j));
                    }
                    line.remove(j);
                    
                } else {
                    
                    doIndex = line.get(line.size() - 1).getIndex();
                    line.remove(line.size() - 1);
                }
                
                Statement statement = new Statement(getSourceEntry(), line.getLineNumber());
                statement.addAll(line);
                
                Block following = new Block(getSourceEntry());
                following.setHeader(blockHeader);
                int blockLevel = 0;
                
                List<Line> subLines = new ArrayList<>();
                int j = i + 1;
                while (true) {
                    
                    if (j < lines.size()) {
                        
                        Line subLine = lines.remove(j);
                        if (subLine.get(0).getToken().isBlocking() || subLine.containsToken(Token.DO)) {
                            
                            blockLevel++;
                            
                        } else if (subLine.get(0).getToken() == Token.END) {
                            
                            if (blockLevel == 0) {
                                
                                break;
                                
                            } else {
                                
                                blockLevel--;
                            }
                        }
                        
                        subLines.add(subLine);
                        
                    } else {
                        
                        Errors.reportSyntaxError("Block does not terminate.", getSourceEntry(), line.getLineNumber(), doIndex);
                        break;
                    }
                }
                
                following.addAll(parseBlock(subLines));
                
                items.add(statement);
                
                if (!following.isEmpty()) {
                    
                    items.add(following);
                }
                
            } else {
                
                Statement statement = new Statement(getSourceEntry(), line.getLineNumber());
                statement.addAll(line);
                items.add(statement);
            }
        }
        
        return items;
    }
}
