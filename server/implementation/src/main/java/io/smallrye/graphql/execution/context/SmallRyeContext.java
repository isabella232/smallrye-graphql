package io.smallrye.graphql.execution.context;

import static io.smallrye.graphql.SmallRyeGraphQLServerMessages.msg;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.json.JsonArray;
import javax.json.JsonObject;

import graphql.ExecutionInput;
import graphql.language.Document;
import graphql.schema.DataFetchingEnvironment;
import io.smallrye.graphql.api.Context;
import io.smallrye.graphql.execution.QueryCache;
import io.smallrye.graphql.schema.model.Field;

/**
 * Implements the Context from MicroProfile API.
 *
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
public class SmallRyeContext implements Context {

    private JsonObject request;
    private String executionId;
    private Field field;
    private String fieldName;
    private Map<String, ?> arguments;
    private Map<?, ?> metaDatas;
    private Map<?, ?> localMetaDatas;
    private Object source;
    private String path;
    private JsonArray selectedFields;
    private JsonArray selectedAndSourceFields;
    private String operationType;
    private List<String> requestedOperationTypes;
    private String parentTypeName;
    private String operationName;
    private DataFetchingEnvironment dataFetchingEnvironment;
    private ExecutionInput executionInput;
    private QueryCache queryCache;
    private DocumentSupplier documentSupplier;

    @Override
    public JsonObject getRequest() {
        return this.request;
    }

    public void setRequest(JsonObject request) {
        this.request = request;
    }

    @Override
    public String getExecutionId() {
        return this.executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    @Override
    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    @Override
    public <ARG> Map<String, ARG> getArguments() {
        return (Map<String, ARG>) this.arguments;
    }

    public <ARG> void setArguments(Map<String, ARG> arguments) {
        this.arguments = arguments;
    }

    @Override
    public <SRC> SRC getSource() {
        return (SRC) this.source;
    }

    public <SRC> void setSource(SRC source) {
        this.source = source;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public JsonArray getSelectedFields() {
        return this.selectedFields;
    }

    public void setSelectedFields(JsonArray selectedFields) {
        this.selectedFields = selectedFields;
    }

    @Override
    public JsonArray getSelectedAndSourceFields() {
        return selectedAndSourceFields;
    }

    public void setSelectedAndSourceFields(JsonArray selectedAndSourceFields) {
        this.selectedAndSourceFields = selectedAndSourceFields;
    }

    @Override
    public String getOperationType() {
        return this.operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public List<String> getRequestedOperationTypes() {
        return this.requestedOperationTypes;
    }

    public void setRequestedOperationTypes(List<String> requestedOperationTypes) {
        this.requestedOperationTypes = requestedOperationTypes;
    }

    @Override
    public Optional<String> getParentTypeName() {
        if (this.parentTypeName != null) {
            return Optional.of(this.parentTypeName);
        }
        return Optional.empty();
    }

    public void setParentTypeName(String parentTypeName) {
        this.parentTypeName = parentTypeName;
    }

    @Override
    public Optional<String> getOperationName() {
        if (this.operationName != null) {
            return Optional.of(this.operationName);
        }
        return Optional.empty();
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public DataFetchingEnvironment getDataFetchingEnvironment() {
        return dataFetchingEnvironment;
    }

    public void setDataFetchingEnvironment(DataFetchingEnvironment dataFetchingEnvironment) {
        this.dataFetchingEnvironment = dataFetchingEnvironment;
    }

    public ExecutionInput getExecutionInput() {
        return executionInput;
    }

    public void setExecutionInput(ExecutionInput executionInput) {
        this.executionInput = executionInput;
    }

    public QueryCache getQueryCache() {
        return queryCache;
    }

    public void setQueryCache(QueryCache queryCache) {
        this.queryCache = queryCache;
    }

    public DocumentSupplier getDocumentSupplier() {
        return documentSupplier;
    }

    public void setDocumentSupplier(DocumentSupplier documentSupplier) {
        this.documentSupplier = documentSupplier;
    }

    @Override
    public <T> T unwrap(Class<T> wrappedType) {
        // We only support DataFetchingEnvironment, ExecutionInput and Document at this point
        if (wrappedType.equals(DataFetchingEnvironment.class)) {
            return (T) getDataFetchingEnvironment();
        } else if (wrappedType.equals(ExecutionInput.class)) {
            return (T) getExecutionInput();
        } else if (wrappedType.equals(Document.class)) {
            if (getExecutionInput() != null && getQueryCache() != null) {
                DocumentSupplier documentSupplier = new DocumentSupplier(executionInput, queryCache);
                return (T) documentSupplier.get();
            }
            return null;
        }
        throw msg.unsupportedWrappedClass(wrappedType.getName());
    }
}
