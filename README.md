# OCTRI Common Library

This package contains the common classes useful for the development of OCTRI Spring-based web applications, including:

* Utility classes for working with Mustache views
* Abstract classes and interfaces
* Patterns for method security
* Configuration class for overriding date/time formats in Mustache


## Using This Package

To use this package, add it to your `pom.xml` file.

```xml
	<dependency>
		<groupId>org.octri.common</groupId>
		<artifactId>common_lib</artifactId>
		<version>${common_lib.version}</version>
	</dependency>
```

## Implementation

The library is implemented using [Spring Boot](https://spring.io/projects/spring-boot). For a detailed list of dependencies, see [pom.xml](./pom.xml).

## Custom Date Formatting in Mustache Templates

To use custom date formats in your application, set the common properties. For example, to use the U.S. Date Pattern:

* octri.common.view.date-pattern=MM-dd-yyyy
* octri.common.view.date-pattern-regex=[0-9]{2}-[0-9]{2}-[0-9]{4}
* octri.common.view.date-time-pattern=MM-dd-yyyy HH:mm a

In your @ControllerAdvice class, autowire the ViewConfig component and add this property to your model:

```
model.addAttribute("viewConfig", viewConfig.getProperties());
```

Other OCTRI libraries will look for this model property when displaying dates in forms. Your application should follow the same pattern to work with custom dates. For example:

```
<div class="form-group">
    <label for="start_date" class="form-label">Start date</label>
    <div class="input-group date-control">
        <input type="text" class="form-control" id="start_date" name="startDate" value="{{#startDate}}{{.}}{{/startDate}}"
            pattern="{{viewConfig.datePatternRegex}}" placeholder="{{viewConfig.datePatternPlaceholder}}" autocomplete="off"
            data-provide="datepicker" data-date-format="{{viewConfig.datePattern}}">
        <span class="input-group-text rounded-end"><i class="far fa-calendar-alt"></i></span>
    </div>
    <div class="invalid-feedback">Value must be formatted {{viewConfig.datePatternPlaceholder}}</div>
</div>
```
