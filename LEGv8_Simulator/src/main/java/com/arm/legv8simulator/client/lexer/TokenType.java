package com.arm.legv8simulator.client.lexer;

import com.arm.legv8simulator.client.parser.Parser;

/**
 * The <code>TokenType</code> enumeration uses regular expressions to define
 * all valid lexical tokens in the LEGv8 assembly language.
 *
 * @author Jonathan Wright, 2016
 */

/* Instruction mnemonics and immediates which contain letters from the English alphabet
 * must contain either all upper case letters or all lower case letters; a mixture
 * is not permitted.
 */
public enum TokenType {

	// The order in which these enums are defined is important to get the longest RegExp match
	LBRACKET("\\[", 1, "["),
	RBRACKET("\\]", 2, "]"),
	COMMA("\\,", 3, ","),
	// FLOAT_IMMEDIATE must come before IMMEDIATE so "0.5" is not consumed as integer "0"
	FLOAT_IMMEDIATE("#?-?[0-9]*\\.[0-9]+", 4, "FLOAT_IMMEDIATE"),
	IMMEDIATE("#?-?0[xX][0-9a-fA-F]+|#?-?[0-9]+", 5, "IMMEDIATE"),
	XREGISTER("[Xx][12][0-9]|[Xx]30|[Xx][0-9]|XZR|xzr|SP|sp|LR|lr|FP|fp|IP[01]|ip[01]+", 6, "XREGISTER"),
	WREGISTER("[Ww][12][0-9]|[Ww]30|[Ww][0-9]|WZR|wzr|WSP|wsp", 7, "WREGISTER"),
	SREGISTER("[Ss][12][0-9]|[Ss]30|[Ss][0-9]+", 8, "SREGISTER"),
	DREGISTER("[Dd][12][0-9]|[Dd]30|[Dd][0-9]+", 9, "DREGISTER"),
	XMNEMONIC_R("BR[ \t]+|br[ \t]+", 10, "XMNEMONIC"),
	XMNEMONIC_RISI("MOV[ZK][ \t]+|mov[zk][ \t]+", 11, "XMNEMONIC"),
	XMNEMONIC_RI("CMPI[ \t]+|cmpi[ \t]+", 12, "XMNEMONIC"),
	XMNEMONIC_RR("CMP[ \t]+|MOV[ \t]+|cmp[ \t]+|mov[ \t]+", 13, "XMNEMONIC"),
	SMNEMONIC_RR("FCMPS[ \t]+|fcmps[ \t]+", 14, "SMNEMONIC"),
	DMNEMONIC_RR("FCMPD[ \t]+|fcmpd[ \t]+", 15, "DMNEMONIC"),
	XMNEMONIC_RRI("ADDIS?[ \t]+|SUBIS?[ \t]+|ANDIS?[ \t]+|ORRI[ \t]+|EORI[ \t]+|LS[LR][ \t]+|addis?[ \t]+|subis?[ \t]+|andis?[ \t]+|orri[ \t]+|eori[ \t]+|ls[lr][ \t]+", 16, "XMNEMONIC"),
	XMNEMONIC_RRR("ADDS?[ \t]+|SUBS?[ \t]+|ANDS?[ \t]+|MUL[ \t]+|SMULH[ \t]+|UMULH[ \t]+|SDIV[ \t]+|UDIV[ \t]+|ORR[ \t]+|EOR[ \t]+|adds?[ \t]+|subs?[ \t]+|ands?[ \t]+|mul[ \t]+|smulh[ \t]+|umulh[ \t]+|sdiv[ \t]+|udiv[ \t]+|orr[ \t]+|eor[ \t]+", 17, "XMNEMONIC"),
	SMNEMONIC_RRR("FADDS[ \t]+|FSUBS[ \t]+|FDIVS[ \t]+|FMULS[ \t]+|fadds[ \t]+|fsubs[ \t]+|fmuls[ \t]+|fdivs[ \t]+", 18, "SMNEMONIC"),
	DMNEMONIC_RRR("FADDD[ \t]+|FSUBD[ \t]+|FDIVD[ \t]+|FMULD[ \t]+|faddd[ \t]+|fsubd[ \t]+|fmuld[ \t]+|fdivd[ \t]+", 19, "DMNEMONIC"),
	XMNEMONIC_RM("LDURSW[ \t]+|LDUR[HB]?[ \t]+|LDXR[ \t]+|STUR[WHB]?[ \t]+|ldursw[ \t]+|ldur[hb]?[ \t]+|ldxr[ \t]+|stur[whb]?[ \t]+", 20, "XMNEMONIC"),
	SMNEMONIC_RM("LDURS[ \t]+|STURS[ \t]+|ldurs[ \t]+|sturs[ \t]+", 21, "SMNEMONIC"),
	DMNEMONIC_RM("LDURD[ \t]+|STURD[ \t]+|ldurd[ \t]+|sturd[ \t]+", 22, "DMNEMONIC"),
	XMNEMONIC_RRM("STXR[ \t]+|stxr[ \t]+", 23, "XMNEMONIC"),
	MNEMONIC_L("B\\.[GNL]E[ \t]+|B\\.[HL]S[ \t]+|B\\.[LG]T[ \t]+|B\\.LO[ \t]+|B\\.[MH]I[ \t]+|B\\.EQ[ \t]+|B\\.V[SC][ \t]+|B\\.PL[ \t]+|B[RL]?[ \t]+|b\\.[gnl]e[ \t]+|b\\.[hl]s[ \t]+|b\\.[lg]t[ \t]+|b\\.lo[ \t]+|b\\.[mh]i[ \t]+|b\\.eq[ \t]+|b\\.v[sc][ \t]+|b\\.pl[ \t]+|b[rl]?[ \t]+", 24, "MNEMONIC"),
	XMNEMONIC_RL("CBN?Z[ \t]+|cbn?z[ \t]+|LDA[ \t]+|lda[ \t]+|ADRP[ \t]+|adrp[ \t]+", 25, "XMNEMONIC"),
	SMNEMONIC_RI("FMOVS[ \t]+|fmovs[ \t]+", 26, "SMNEMONIC"),
	DMNEMONIC_RI("FMOVD[ \t]+|fmovd[ \t]+", 27, "DMNEMONIC"),
	LABEL("[A-Za-z0-9_]+:", 28, "LABEL"),
	IDENTIFIER("[A-Za-z0-9_]+", 29, "IDENTIFIER"),
	WHITESPACE("[ \t]+", 30, "WHITESPACE"),
	RELOCATION(":lo12:[A-Za-z0-9_]+", 31, "RELOCATION"),
	ERROR("[^\\s]+", 32, "ERROR");

	/**
	 * The regular expression pattern used to specify this <code>TokenType</code>
	 */
	public final String pattern;

	/**
	 * The String representation of this <code>TokenType</code> to be presented to
	 * the user in <code>Lexer</code> and <code>Parser</code> error messages.
	 *
	 * @see Lexer
	 * @see Parser
	 */
	public final String tokenStr;

	/**
	 * The ID number used by the <code>Lexer</code> to identify each <code>TokenType</code>
	 */
	public final int groupNumber;

	/**
	 * @param pattern		The regular expression pattern used to specify this <code>TokenType</code>
	 * @param groupNumber	The String representation of this <code>TokenType</code> to be presented to
	 *						the user in <code>Lexer</code> and <code>Parser</code> error messages.
	 * @param tokenStr		The ID number used by the <code>Lexer</code> to identify each <code>TokenType</code>
	 */
	private TokenType(String pattern, int groupNumber, String tokenStr) {
		this.pattern = pattern;
		this.groupNumber = groupNumber;
		this.tokenStr = tokenStr;
	}

	/**
	 * @return	tokenStr
	 *
	 */
	@Override
	public String toString() {
		return tokenStr;
	}
}
