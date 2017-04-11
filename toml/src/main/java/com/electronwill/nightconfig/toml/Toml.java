package com.electronwill.nightconfig.toml;

import com.electronwill.nightconfig.core.serialization.CharacterInput;
import com.electronwill.nightconfig.core.serialization.CharsWrapper;
import com.electronwill.nightconfig.core.serialization.Utils;

/**
 * @author TheElectronWill
 */
final class Toml {

	private static final char[] WHITESPACE_OR_NEWLINE = {'\t', ' ', '\n', '\r'};
	private static final char[] WHITESPACE = {'\t', ' '};
	private static final char[] NEWLINE = {'\n'};
	private static final char[] FORBIDDEN_IN_ALL_BARE_KEYS = {'.', '[', ']', '#', '='};

	/**
	 * Returns the next "useful" character. Skips comments, spaces and newlines.
	 */
	static char readUsefulChar(CharacterInput input) {
		char next = input.readCharAndSkip(WHITESPACE_OR_NEWLINE);
		while (next == '#') {
			input.readCharsUntil(NEWLINE);
			next = input.readCharAndSkip(WHITESPACE_OR_NEWLINE);
		}
		return next;
	}

	/**
	 * Returns the next "useful" character. Skips comments, spaces and newlines.
	 */
	static int readUseful(CharacterInput input) {
		int next = input.readAndSkip(WHITESPACE_OR_NEWLINE);
		while (next == '#') {
			input.readUntil(NEWLINE);
			next = input.readAndSkip(WHITESPACE_OR_NEWLINE);
		}
		return next;
	}

	/**
	 * Reads the next non-space character. Doesn't skip comments.
	 */
	static char readNonSpaceChar(CharacterInput input, boolean skipNewlines) {
		return skipNewlines
			? input.readCharAndSkip(WHITESPACE_OR_NEWLINE)
			: input.readCharAndSkip(WHITESPACE);
	}

	/**
	 * Reads the next non-space character. Doesn't skip comments.
	 */
	static int readNonSpace(CharacterInput input, boolean skipNewlines) {
		return skipNewlines ? input.readAndSkip(WHITESPACE_OR_NEWLINE) : input.readAndSkip(WHITESPACE);
	}

	/**
	 * Reads all the characters before the next newline.
	 */
	static CharsWrapper readLine(CharacterInput input) {
		return input.readCharsUntil(NEWLINE);
	}

	static boolean isValidInBareKey(char c, boolean lenient) {
		if (lenient) { return c > ' ' && !Utils.arrayContains(FORBIDDEN_IN_ALL_BARE_KEYS, c); }
		return (c >= 'a' && c <= 'z')
			   || (c >= 'A' && c <= 'Z')
			   || (c >= '0' && c <= '9')
			   || c == '-'
			   || c == '_';
	}

	static boolean isValidBareKey(CharSequence csq, boolean lenient) {
		for (int i = 0; i < csq.length(); i++) {
			if (!isValidInBareKey(csq.charAt(i), lenient)) { return false; }
		}
		return true;
	}

	private Toml() {}
}