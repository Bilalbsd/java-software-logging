package org.example.spoon;
import org.slf4j.Logger;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
public class Logging extends AbstractProcessor<CtMethod<?>> {
    @Override
    public void process(CtMethod<?> method) {
        CtClass<?> parentClass = method.getParent(CtClass.class);
        // On fait l'instrumentation uniquement pour la classe ProductController
        if ((parentClass != null) && "ProductController".equals(parentClass.getSimpleName())) {
            String methodName = method.getSimpleName();
            // Ignorer les méthodes spécifiques de User (authentification, addUser, getUser, déconnexion)
            if (isUserMethod(methodName)) {
                return;// Ignorer cette méthode

            }
            addLoggerFieldsIfAbsent(parentClass);
            if (isGetProductByIdMethod(method)) {
                // Pour les logs des prix
                replaceGetProductByIdMethod(method);
            } else {
                // Gérer les logs des méthodes de lecture et d'écriture
                handleReadWriteMethodLogging(method);
            }
        }
    }

    private void addLoggerFieldsIfAbsent(CtClass<?> parentClass) {
        addLoggerField(parentClass, "readLogger", "readLogger");
        addLoggerField(parentClass, "writeLogger", "writeLogger");
        addLoggerField(parentClass, "mostExpensiveLogger", "mostExpensiveLogger");
    }

    private void addLoggerField(CtClass<?> parentClass, String fieldName, String loggerName) {
        CtTypeReference<Logger> typeLogger = getFactory().createCtTypeReference(Logger.class);
        if (parentClass.getFields().stream().noneMatch(field -> field.getSimpleName().equals(fieldName))) {
            CtField<Logger> loggerField = getFactory().createCtField(fieldName, typeLogger, ("org.slf4j.LoggerFactory.getLogger(\"" + loggerName) + "\")", ModifierKind.PRIVATE, ModifierKind.STATIC, ModifierKind.FINAL);
            parentClass.addFieldAtTop(loggerField);
        }
    }

    private void replaceGetProductByIdMethod(CtMethod<?> method) {
        // Récupérer l'utilisateur actuel avant d'utiliser dans les logs
        CtStatement getUserStatement = getFactory().createCodeSnippetStatement("User user = getCurrentUser();");
        CtStatement getProductStatement = getFactory().createCodeSnippetStatement("Product product = productService.getProductById(id)");
        CtIf ifStatement = getFactory().createIf();
        ifStatement.setCondition(getFactory().createCodeSnippetExpression("product != null"));
        CtBlock<?> thenBlock = getFactory().createBlock();
        thenBlock.addStatement(getFactory().createCodeSnippetStatement("boolean isMostExpensive = productService.isProductTheMostExpensive(id)"));
        thenBlock.addStatement(getFactory().createCodeSnippetStatement("Logger currentLogger = isMostExpensive ? mostExpensiveLogger : readLogger"));
        CtIf innerIfStatement = getFactory().createIf();
        innerIfStatement.setCondition(getFactory().createCodeSnippetExpression("user != null"));
        // Mise à jour pour générer un log au format JSON
        innerIfStatement.setThenStatement(getFactory().createCodeSnippetStatement("currentLogger.info(\"\\\"message\\\": \\\"getProductById\\\", \\\"User\\\": \\\"{}\\\", \\\"UserID\\\": \\\"{}\\\", \\\"Product\\\": \\\"{}\\\", \\\"ProductID\\\": \\\"{}\\\", \\\"Price\\\": {}\", user.getName(), user.getId(), product.getName(), product.getId(), product.getPrice())"));
        thenBlock.addStatement(innerIfStatement);
        thenBlock.addStatement(getFactory().createCodeSnippetStatement("return ResponseEntity.ok(product)"));
        ifStatement.setThenStatement(thenBlock);
        ifStatement.setElseStatement(getFactory().createCodeSnippetStatement("return (ResponseEntity<?>) ResponseEntity.notFound()"));
        CtBlock<?> newBody = getFactory().createBlock();
        newBody.addStatement(getUserStatement);// Ajout de la récupération de l'utilisateur

        newBody.addStatement(getProductStatement);
        newBody.addStatement(ifStatement);
        method.setBody(newBody);
    }

    private boolean isGetProductByIdMethod(CtMethod<?> method) {
        return ("getProductById".equals(method.getSimpleName()) && (method.getParameters().size() == 1)) && method.getParameters().getFirst().getType().getSimpleName().equals("String");
    }

    private void handleReadWriteMethodLogging(CtMethod<?> method) {
        // Récupérer l'utilisateur actuel avant de loguer
        String logStatement = "User user = getCurrentUser();\n";
        String loggerName = determineLoggerName(method);
        logStatement += String.format("%s.info(\"\\\"message\\\": \\\"%s\\\", \\\"User\\\": \\\"\" + user.getName() + \"\\\", \\\"UserID\\\": \\\"\" + user.getId() + \"\\\"\");", loggerName, method.getSimpleName());
        addLogStatement(method, logStatement);
    }

    private String determineLoggerName(CtMethod<?> method) {
        if (isReadOperation(method)) {
            return "readLogger";
        } else if (isWriteOperation(method)) {
            return "writeLogger";
        } else if (isExpensiveProductSearch(method)) {
            return "mostExpensiveLogger";
        }
        return "logger";// Fallback au logger général

    }

    private boolean isWriteOperation(CtMethod<?> method) {
        String methodName = method.getSimpleName();
        return (methodName.startsWith("add") || methodName.startsWith("update")) || methodName.startsWith("delete");
    }

    private boolean isExpensiveProductSearch(CtMethod<?> method) {
        String methodName = method.getSimpleName();
        return methodName.contains("ExpensiveProductSearch") || methodName.contains("MaxPriceQuery");
    }

    private boolean isReadOperation(CtMethod<?> method) {
        String methodName = method.getSimpleName();
        return (methodName.startsWith("get") || methodName.startsWith("find")) || methodName.startsWith("list");
    }

    private void addLogStatement(CtMethod<?> method, String logStatement) {
        CtCodeSnippetStatement snippet = getFactory().createCodeSnippetStatement(logStatement);
        method.getBody().insertBegin(snippet);
    }

    // Exclure les méthodes de user pour les logs
    private boolean isUserMethod(String methodName) {
        return (("addUser".equals(methodName) || "authenticateUser".equals(methodName)) || "getUser".equals(methodName)) || "logoutUser".equals(methodName);
    }
}
