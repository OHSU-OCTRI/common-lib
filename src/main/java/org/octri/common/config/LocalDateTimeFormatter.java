package org.octri.common.config;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * Adapter to provide a org.springframework.format.Formatter from a java.time.DateTimeFormatter
 */
public final class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

	private final String pattern;

	public LocalDateTimeFormatter(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String print(LocalDateTime date, Locale locale) {
		if (date == null) {
			return "";
		}
		return getFormatter(locale).format(date);
	}

	public String print(LocalDateTime date) {
		if (date == null) {
			return "";
		}
		return getFormatter().format(date);
	}

	@Override
	public LocalDateTime parse(String formatted, Locale locale) throws ParseException {
		if (formatted.length() == 0) {
			return null;
		}
		return LocalDateTime.from(getFormatter(locale).parse(formatted));
	}

	private DateTimeFormatter getFormatter(Locale locale) {
		return DateTimeFormatter.ofPattern(pattern, locale);
	}

	private DateTimeFormatter getFormatter() {
		return DateTimeFormatter.ofPattern(pattern);
	}
}
