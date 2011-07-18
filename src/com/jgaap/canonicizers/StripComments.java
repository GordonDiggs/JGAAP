// Copyright (c) 2009, 2011 by Patrick Juola.   All rights reserved.  All unauthorized use prohibited.  
/**
 **/
package com.jgaap.canonicizers;

import java.awt.Color;

import com.jgaap.generics.Canonicizer;
import com.jgaap.jgaapConstants;

/**
 * Strips comments from source file.
 * Eliminates comments of the following format:
 * * C-style comments        / * like this * /
 * * C++/Java-style comments // like this 
 * * Perl-style comments     # like this
 * 
 * @since 5.0
 * @author Patrick Juola
 **/
public class StripComments extends Canonicizer {

	private enum LanguageEnum {
		UNDEF,
		C,
		JAVA,
		PERL
	};

	@Override
	public String displayName() {
		return "Strip Comments";
	}

	@Override
	public String tooltipText() {
		return "Strips comments from source code";
	}

	@Override
	public String longDescription() {
		return "Strips C, C++, Java, and Perl-style comments from source code";
	}

	@Override
	public boolean showInGUI(){
        	return jgaapConstants.globalParams.getParameter("language").equals("computer");
	}


	@Override
	public Color guiColor() {
		return Color.CYAN;
	}

	/**
	 * strip comments from source code given in argument
	 * 
	 * @param procText
	 *            Array of Characters to be processed
	 * @return modified Array of Characters
	 * 
	 */
	@Override
	public char[] process(char[] procText) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean isPrinting = true;
		LanguageEnum theLang = LanguageEnum.UNDEF;

		
		for (int i = 0; i < procText.length; i++) {

		// case 1 : C-style comments */
			if (theLang == LanguageEnum.UNDEF &&
			    procText[i] == '/' && procText[i+1] == '*') {
				i = i+1;
				theLang = LanguageEnum.C;
				isPrinting = false;
				continue;

			}
			
			if (theLang == LanguageEnum.C &&
			    procText[i] == '*' && procText[i+1] == '/') {
				i = i+1;
				theLang = LanguageEnum.UNDEF;
				isPrinting = true;
				continue;
			}

		// case 2 : Java-style comments */
			if (theLang == LanguageEnum.UNDEF &&
			    procText[i] == '/' && procText[i+1] == '/') {
				i = i+1;
				theLang = LanguageEnum.JAVA;
				isPrinting = false;
				continue;

			}
			
			if (theLang == LanguageEnum.JAVA &&
			    procText[i] == '\n') {
				isPrinting = true;
				theLang = LanguageEnum.UNDEF;
				continue;
			}

		// case 3 : Perl-style comments
			if (theLang == LanguageEnum.UNDEF &&
				procText[i] == '#') {
				theLang = LanguageEnum.PERL;
				isPrinting = false;
				continue;

			}
			
			if (theLang == LanguageEnum.PERL &&
			    procText[i] == '\n') {
				theLang = LanguageEnum.UNDEF;
				isPrinting = true;
				continue;
			}



			if (isPrinting) {
				//System.out.println("Appending "+procText[i]);
				stringBuilder.append(procText[i]);
			}
		}

		return stringBuilder.toString().toCharArray();
	}
}
