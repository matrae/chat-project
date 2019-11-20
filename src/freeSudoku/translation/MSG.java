/**
* Copyright 2005 Victor Ferrer
* 
* Copyright 2006, 2007 Brad Richards (http://richards.kri.ch/)
* 
* This file is part of FreeSudoku.
* 
* FreeSudoku is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
* 
* FreeSudoku is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with FreeSudoku; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
*/
package freeSudoku.translation;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class that use Java's ResourceBundle to implements the functionality of translations.
 * 
 * @author Javier García (jntx)
 * @version 0.1 14/11/2005
 */
public class MSG {

	/**
	 * Path and name of the ResourceBundle properties file.
	 */
	private static final String BUNDLE_NAME = "freeSudoku.translation.FreeSudoku";

	/**
	 * ResourceBundle to read properties file.
	 */
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());

	/**
	 * Private constructor for prevent creation of new instances.
	 */
	private MSG() {
	}

	/**
	 * Method to obtain the string for a gui component.
	 * 
	 * @param key
	 *            Key of the gui component.
	 * @return String associated with the key, or !key! if key not found.
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Method to obtain the mnemonic for a gui component.
	 * 
	 * @param key
	 *            Key of the gui component.
	 * @return Char associated with the key, or empty char ' ' if key not found.
	 */
	public static char getMnemonic(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key).charAt(0);
		} catch (MissingResourceException e) {
			return ' ';
		}
	}

	/**
	 * Method to establish the Locale for this manager.
	 * 
	 * @param locale
	 *            New Locale instance.
	 */
	public static void setLocale(Locale locale) {
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}

	/**
	 * Method to obtain available Locale's supported by the game.
	 * 
	 * @return Array with Locale's
	 */
	public static Locale[] getAvailableLocales() {
		return new Locale[] { new Locale("ca"), new Locale("de"), new Locale("en"), new Locale("es"), new Locale("fr") };
	}

	/**
	 * Method to obtain available Locale Name's supported by the game.
	 * 
	 * @param allToUpper
	 *            Flag that indicates when all the characters may be toUpperCase.
	 * @return Array with Locale Name's
	 */
	public static String[] getAvailableLocaleNames(boolean allToUpper) {
		Locale locs[] = getAvailableLocales();
		String res[] = new String[locs.length];
		for (int i = 0; i < locs.length; i++) {
			if (allToUpper) {
				res[i] = locs[i].getDisplayLanguage().toUpperCase();
			} else {
				res[i] = firstToUpperCase(locs[i].getDisplayLanguage());
			}
		}
		return res;
	}

	/**
	 * Method to pass the first character of string toUpperCase.
	 * 
	 * @param in
	 *            In String.
	 * @return Out String.
	 */
	private static String firstToUpperCase(String in) {
		// Obtain array
		char array[] = in.toCharArray();

		// To Uppercase the first char
		String aux = new String("" + in.charAt(0));
		aux = aux.toUpperCase();

		// Store new string
		StringBuffer res = new StringBuffer();
		res.append(aux);
		res.append(array, 1, array.length - 1);

		// Return String
		return res.toString();
	}

}
