package org.octri.common.config;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * Adapter to provide a org.springframework.format.Formatter from a java.time.DateTimeFormatter
 */
public final class LocalDateFormatter implements Formatter<LocalDate> {

	private final String pattern;

	public LocalDateFormatter(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String print(LocalDate date, Locale locale) {
		if (date == null) {
			return "";
		}
		return getFormatter(locale).format(date);
	}

	public String print(LocalDate date) {
		if (date == null) {
			return "";
		}
		return getFormatter().format(date);
	}

	@Override
	public LocalDate parse(String formatted, Locale locale) throws ParseException {
		if (formatted.length() == 0) {
			return null;
		}
		return LocalDate.from(getFormatter(locale).parse(formatted));
	}

	private DateTimeFormatter getFormatter(Locale locale) {
		return DateTimeFormatter.ofPattern(pattern, locale);
	}

	private DateTimeFormatter getFormatter() {
		return DateTimeFormatter.ofPattern(pattern);
	}
}
