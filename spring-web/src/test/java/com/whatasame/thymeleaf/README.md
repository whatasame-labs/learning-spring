# Thymeleaf

This test case shows how to use Thymeleaf in Spring MVC.

## Why Thymeleaf?

- Thymeleaf template can be viewed as a valid HTML5 document. You can put default values in the template, so that
  the page can be viewed independently.
- Thymeleaf is built as JAR and can be directly included in your application, without any need for deployment or any
  other installation. In case of JSP built as WAR, it needs to be deployed on a servlet container such as Tomcat.

## Standard expression syntax

### Variable expression

`${book.author}` is same as next expression.

```
((Book) context.getVariable("book")).getAuthor();
```

You can access built-in Thymeleaf objects by using `#` prefix. For example, `#numbers` supports common operations on
numbers like this.

```
${#numbers.sequence(1,5)} // return [1,2,3,4,5]
```

## Standard dialect

### text expression

`th:text` replace tag body with the result of expression evaluation.

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head></head>
<body>
  <h1>What I ate today</h1>
  <p>Breakfast: <span th:text="${breakfast}"></span></p>
  <p>Launch: <span th:text="${launch}"></span></p>
  <p>Dinner: <span th:text="${dinner}"></span></p>
</body>
</html>
```

See [meals.html](../../../../../../main/resources/templates/thymeleaf/meals.html)

### for-each expression

`th:each` repeats the element it's in as many times as specified by the iterable object returned by its expression.

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head></head>
<body>
  <tr th:each="book: ${books}">
    <td th:text="${book.title}">title</td>
    <td th:text="${book.author}">author</td>
  </tr>
</body>
</html>
```

See [library.html](../../../../../../main/resources/templates/thymeleaf/library.html)

## Reference

- [Spring MVC view layer: Thymeleaf vs. JSP | Thymeleaf](https://www.thymeleaf.org/doc/articles/thvsjsp.html)
- [Getting started with the Standard dialects in 5 minutes | Thymeleaf](https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html)
- [Serving Web Content with Spring MVC | Spring Guides](https://spring.io/guides/gs/serving-web-content/)
