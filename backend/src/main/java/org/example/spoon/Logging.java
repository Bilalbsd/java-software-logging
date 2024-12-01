package org.example.spoon;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.declaration.*;
import spoon.reflect.reference.CtTypeReference;
public class Logging extends AbstractProcessor<CtMethod<?>> {
    private static final Set<String> USER_METHODS = Set.of("addUser", "authenticateUser", "getUser", "logoutUser");

    @Override
    public void process(CtMethod<?> method) {
        CtClass<?> parentClass = method.getParent(CtClass.class);
        if (shouldProcessMethod(method, parentClass)) {
            addLoggerFieldsIfAbsent(parentClass);
            if (isGetProductByIdMethod(method)) {
                replaceGetProductByIdMethod(method);
            } else {
                handleReadWriteMethodLogging(method);
            }
        }
    }

    private boolean shouldProcessMethod(CtMethod<?> method, CtClass<?> parentClass) {
        return (((parentClass != null) && "ProductController".equals(parentClass.getSimpleName())) && (!USER_METHODS.contains(method.getSimpleName()))) && (!"getCurrentUser".equals(method.getSimpleName()));
    }

    private void addLoggerFieldsIfAbsent(CtClass<?> parentClass) {
        List.of("readLogger", "writeLogger", "mostExpensiveLogger").forEach(loggerName -> addLoggerFieldIfAbsent(parentClass, loggerName));
    }

    private void addLoggerFieldIfAbsent(CtClass<?> parentClass, String fieldName) {
        if (parentClass.getFields().stream().noneMatch(field -> field.getSimpleName().equals(fieldName))) {
            CtField<Logger> loggerField = createLoggerField(fieldName);
            parentClass.addFieldAtTop(loggerField);
        }
    }

    private CtField<Logger> createLoggerField(String fieldName) {
        CtTypeReference<Logger> typeLogger = getFactory().createCtTypeReference(Logger.class);
        String loggerInitialization = String.format("org.slf4j.LoggerFactory.getLogger(\"%s\")", fieldName);
        return getFactory().createCtField(fieldName, typeLogger, loggerInitialization, ModifierKind.PRIVATE, ModifierKind.STATIC, ModifierKind.FINAL);
    }

    private void replaceGetProductByIdMethod(CtMethod<?> method) {
        CtBlock<?> newBody = getFactory().createBlock();
        newBody.addStatement(createCodeSnippet("User user = getCurrentUser()"));
        newBody.addStatement(createCodeSnippet("Product product = productService.getProductById(id)"));
        CtIf ifStatement = createGetProductByIdIfStatement();
        newBody.addStatement(ifStatement);
        method.setBody(newBody);
    }

    private CtIf createGetProductByIdIfStatement() {
        CtIf ifStatement = getFactory().createIf();
        ifStatement.setCondition(createCodeSnippetExpression());
        CtBlock<?> thenBlock = getFactory().createBlock();
        thenBlock.addStatement(createCodeSnippet("boolean isMostExpensive = productService.isProductTheMostExpensive(id)"));
        thenBlock.addStatement(createCodeSnippet("Logger currentLogger = isMostExpensive ? mostExpensiveLogger : readLogger"));
        thenBlock.addStatement(createUserLoggingStatement());
        thenBlock.addStatement(createCodeSnippet("return ResponseEntity.ok(product)"));
        ifStatement.setThenStatement(thenBlock);
        ifStatement.setElseStatement(createCodeSnippet("return (ResponseEntity<?>) ResponseEntity.notFound()"));
        return ifStatement;
    }

    private CtStatement createUserLoggingStatement() {
        return createCodeSnippet((((("if (user != null) {" + "    currentLogger.info(\"\\\"method\\\": \\\"getProductById\\\", ") + "\\\"username\\\": \\\"{}\\\", \\\"UserID\\\": \\\"{}\\\", ") + "\\\"productName\\\": \\\"{}\\\", \\\"productID\\\": \\\"{}\\\", \\\"price\\\": {}\", ") + "user.getName(), user.getId(), product.getName(), product.getId(), product.getPrice());") + "}");
    }

    private void handleReadWriteMethodLogging(CtMethod<?> method) {
        String loggerName = determineLoggerName(method);
        String logStatement = String.format(("User user = getCurrentUser();\n" + "%s.info(\"\\\"method\\\": \\\"%s\\\", \\\"username\\\": \\\"\" + user.getName() + ") + "\"\\\", \\\"UserID\\\": \\\"\" + user.getId() + \"\\\"\")", loggerName, method.getSimpleName());
        addLogStatement(method, logStatement);
    }

    private String determineLoggerName(CtMethod<?> method) {
        if (isReadOperation(method))
            return "readLogger";

        if (isWriteOperation(method))
            return "writeLogger";

        if (isExpensiveProductSearch(method))
            return "mostExpensiveLogger";

        return "logger";
    }

    private void addLogStatement(CtMethod<?> method, String logStatement) {
        CtCodeSnippetStatement snippet = createCodeSnippet(logStatement);
        method.getBody().insertBegin(snippet);
    }

    private CtCodeSnippetStatement createCodeSnippet(String code) {
        return getFactory().createCodeSnippetStatement(code);
    }

    private CtExpression<Boolean> createCodeSnippetExpression() {
        return getFactory().createCodeSnippetExpression("product != null");
    }

    private boolean isGetProductByIdMethod(CtMethod<?> method) {
        return ("getProductById".equals(method.getSimpleName()) && (method.getParameters().size() == 1)) && "String".equals(method.getParameters().getFirst().getType().getSimpleName());
    }

    private boolean isWriteOperation(CtMethod<?> method) {
        return method.getSimpleName().matches("^(add|update|delete).*");
    }

    private boolean isExpensiveProductSearch(CtMethod<?> method) {
        return method.getSimpleName().matches(".*(ExpensiveProductSearch|MaxPriceQuery).*");
    }

    private boolean isReadOperation(CtMethod<?> method) {
        return method.getSimpleName().matches("^(get|find|list).*");
    }
}
