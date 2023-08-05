/*
 * File updated ~ 31 - 7 - 2023 ~ Leaf
 */

package leaf.soulhome.datagen.patchouli.categories.data;

import java.util.Locale;

/**
 * Refer to <a href="https://vazkiimods.github.io/Patchouli/docs/patchouli-basics/text-formatting/">Patchouli documentation</a>
 */
public class PatchouliTextFormat
{
	private static final StringBuilder s_stringBuilder = new StringBuilder();


	public static String Item(String input)
	{
		return format(input, "$(item)");
	}

	public static String Thing(String input)
	{
		return format(input, "$(thing)");
	}

	public static String Obfuscate(String input)
	{
		return format(input, "$(obf)");
	}

	public static String Italics(String input)
	{
		return format(input, "$(italics)");
	}

	public static String Bold(String input)
	{
		return format(input, "$(bold)");
	}

	public static String Strikethrough(String input)
	{
		return format(input, "$(strike)");
	}

	public static String LineBreak(String input)
	{
		return format(input, "$(br)");
	}

	public static String DoubleLineBreak(String input)
	{
		return format(input, "$(br2)");
	}

	public static String list(String input)
	{
		return format(input, "$(li)");
	}

	public static String KeyBind(String input)
	{
		return format("", "$(k:%s)".formatted(input));
	}

	public static String LinkEntry(String input, String entryToLinkTo)
	{
		//example
		//$(l:cosmere:allomancy/allomantic_aluminum)aluminum$()
		return format(input, "$(l:%s)".formatted(entryToLinkTo.toLowerCase(Locale.ROOT)));
	}

	// makes the tooltip text show over the input when mouse hovers over.
	public static String Tooltip(String input, String tooltipText)
	{
		return format(input, "$(t:%s)".formatted(tooltipText));
	}

	public static String PlayerName()
	{
		return format("", "$(playername)");
	}


	private static String format(String input, String formatCode)
	{
		s_stringBuilder.append(formatCode);
		s_stringBuilder.append(input);
		//Clears any formatting currently applied
		if (!input.contains("$()"))
		{
			//Only need to clear once
			s_stringBuilder.append("$()");
		}

		final String s = s_stringBuilder.toString();
		s_stringBuilder.setLength(0);
		return s;
	}


}
