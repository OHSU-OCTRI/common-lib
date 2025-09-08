package org.octri.common.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.TemplateLoader;

/**
 * Configuration for registering data formatters.
 *
 * <h2>Design notes:</h2>
 * <p>
 * The typical Spring methods for configuring formatters do not work with Mustache templates. However, JMustache does
 * provide a mechanism to define custom formatters when creating a compiler. The main difference from the general
 * Spring approach is that these formatters do not have access to the current Locale.
 * </p>
 * <p>
 * If we want to support formatting based on the Locale, this functionality should likely move to a custom Lambda
 * provided in a HandlerInterceptor class (ex. <code>{{#format}}{{myValue}}{{/format}}</code>). See
 * <a href=
 * "https://source.ohsu.edu/OCTRI-Apps/spring-boot-archetype/blob/3d973951dd1af0219ef30eab03d99315626d17b7/src/main/resources/archetype-resources/src/main/java/config/LocalizationMessageInterceptor.java">LocalizationMessageInterceptor</a>.
 * </p>
 *
 * @see <a href=
 *      "https://docs.spring.io/spring-framework/reference/core/validation/format.html">https://docs.spring.io/spring-framework/reference/core/validation/format.html</a>
 * @see <a href=
 *      "https://docs.spring.io/spring-framework/reference/core/validation/convert.html">https://docs.spring.io/spring-framework/reference/core/validation/convert.html</a>
 * @see <a href=
 *      "https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/mustache/MustacheAutoConfiguration.java">https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/mustache/MustacheAutoConfiguration.java</a>
 * @see <a href=
 *      "https://github.com/samskivert/jmustache#user-defined-object-formatting">https://github.com/samskivert/jmustache#user-defined-object-formatting</a>
 */
@Configuration
@ConfigurationProperties(prefix = "octri.common.view")
public class ViewConfig {

	private String datePattern = "yyyy-MM-dd";
	private String datePatternRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Custom Mustache Compiler which registers a formatter.
	 *
	 * @param mustacheTemplateLoader
	 * @return
	 */
	@Bean
	public Mustache.Compiler mustacheCompiler(TemplateLoader mustacheTemplateLoader) {
		var compiler = Mustache.compiler().withLoader(mustacheTemplateLoader);
		if (StringUtils.isAllBlank(getDatePattern()) && StringUtils.isAllBlank(getDateTimePattern())) {
			return compiler;
		}
		return compiler.withFormatter(mustacheFormatter());
	}

	/**
	 * Creates a new Mustache Formatter that gets applied to every model item passed to the template.
	 *
	 * This method currently only provides specialized behavior for objects of type LocalDate and LocalDateTime and uses
	 * the default behavior of calling `String.valueOf` for everything else. If formatters are needed for other types
	 * this method should introduce a mechanism for getting the appropriate formatter based on the object class.
	 *
	 * @return
	 */
	private Mustache.Formatter mustacheFormatter() {
		LocalDateFormatter dateFormatter = localDateFormatter();
		LocalDateTimeFormatter dateTimeFormatter = localDateTimeFormatter();
		return new Mustache.Formatter() {

			public String format(Object value) {
				if (value instanceof LocalDate) {
					return dateFormatter.print((LocalDate) value);
				} else if (value instanceof LocalDateTime) {
					return dateTimeFormatter.print((LocalDateTime) value);
				} else {
					return String.valueOf(value);
				}
			}
		};
	}

	/**
	 * LocalDateFormatter bean. Note that this must be a bean for use in Controllers.
	 *
	 * @return
	 */
	@Bean
	public LocalDateFormatter localDateFormatter() {
		return new LocalDateFormatter(getDatePattern());
	}

	/**
	 * LocalDateTimeFormatter bean. Note that this must be a bean for use in Controllers.
	 *
	 * @return
	 */
	@Bean
	public LocalDateTimeFormatter localDateTimeFormatter() {
		return new LocalDateTimeFormatter(getDateTimePattern());
	}

	/**
	 * Properties that may be useful in templates.
	 *
	 * @return
	 */
	public Map<String, String> getProperties() {
		return Map.of("datePattern", getDatePattern(),
				"datePatternRegex", getDatePatternRegex(),
				"datePatternPlaceholder", getDatePattern().toLowerCase(),
				"dateTimePattern", getDateTimePattern());
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String localDateFormat) {
		this.datePattern = localDateFormat;
	}

	public String getDatePatternRegex() {
		return datePatternRegex;
	}

	public void setDatePatternRegex(String datePatternRegex) {
		this.datePatternRegex = datePatternRegex;
	}

	public String getDateTimePattern() {
		return dateTimePattern;
	}

	public void setDateTimePattern(String dateTimePattern) {
		this.dateTimePattern = dateTimePattern;
	}

}
