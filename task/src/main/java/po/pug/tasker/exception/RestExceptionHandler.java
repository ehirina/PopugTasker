package po.pug.tasker.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
//    @ExceptionHandler(Throwable.class)
//    protected ResponseEntity<UtentiErrorResponse> handleThrowable(Throwable ex) {
//        log.error(ex.getMessage(), ex);
//        var lte = (ex instanceof UtentiException) ?
//            (UtentiException) ex :
//            new UtentiException(UtentiErrorRecord.UNKNOWN, ex, ex.getMessage());
//        return createResponse(lte);
//    }
//
//    @ExceptionHandler(ValidationException.class)
//    protected ResponseEntity<UtentiErrorResponse> handleMethodArgumentTypeMismatchException(
//        ValidationException ex) {
//        var lte = new UtentiException(UtentiErrorRecord.VALIDATION, ex, ex.getMessage());
//        return createResponse(lte);
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    protected ResponseEntity<UtentiErrorResponse> handleMethodArgumentTypeMismatchException(
//        MethodArgumentTypeMismatchException ex) {
//        var rootCause = ex.getMostSpecificCause();
//        var message = String.format("Field error on field '%s': %s", ex.getName(), rootCause.getMessage());
//        var lte = new UtentiException(UtentiErrorRecord.VALIDATION, ex, message);
//        return createResponse(lte);
//    }
//
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    protected ResponseEntity<UtentiErrorResponse> handleMissingServletRequestParameterException(
//        MissingServletRequestParameterException ex
//    ) {
//        var message = String.format("Parameter '%s' is missing", ex.getParameterName());
//        var lte = new UtentiException(UtentiErrorRecord.INVALID_REQUEST, ex, message);
//        return createResponse(lte);
//    }
//
//    @ExceptionHandler(MissingRequestHeaderException.class)
//    protected ResponseEntity<UtentiErrorResponse> handleMissingRequestHeaderException(
//        MissingRequestHeaderException ex
//    ) {
//        var message = String.format("Header '%s' is missing", ex.getHeaderName());
//        var lte = new UtentiException(UtentiErrorRecord.INVALID_REQUEST, ex, message);
//        return createResponse(lte);
//    }
//
//    @ExceptionHandler(BindException.class)
//    protected ResponseEntity<UtentiErrorResponse> handleMethodArgumentNotValidException(BindException ex) {
//        var fieldErrors = ex.getBindingResult().getFieldErrors();
//        var message = fieldErrors.stream()
//            .sorted((Comparator.comparing(FieldError::getObjectName).thenComparing(FieldError::getField)))
//            .map(error ->
//                String.format("Field error in object '%s' on field '%s': rejected value [%s]",
//                    error.getObjectName(),
//                    error.getField(),
//                    error.getRejectedValue()
//                )
//            ).collect(Collectors.joining("; "));
//        var lte = new UtentiException(UtentiErrorRecord.VALIDATION, ex, message);
//        return createResponse(lte);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    protected ResponseEntity<UtentiErrorResponse> handleHttpMessageNotReadableException(
//        HttpMessageNotReadableException ex) {
//        var rootCause = ex.getMostSpecificCause();
//        var message = rootCause.getMessage().split("\\n")[0];
//        if (rootCause instanceof JsonMappingException) {
//            var jsonMappingException = (JsonMappingException) rootCause;
//            var path = jsonMappingException.getPath();
//            if (!CollectionUtils.isEmpty(path)) {
//                String fields = path.stream()
//                    .map(JsonMappingException.Reference::getFieldName)
//                    .collect(Collectors.joining(", "));
//                message += ": " + fields;
//            }
//        }
//        var lte = new UtentiException(UtentiErrorRecord.INVALID_REQUEST, ex, message);
//        return createResponse(lte);
//    }
//
//    private ResponseEntity<UtentiErrorResponse> createResponse(UtentiException lte) {
//        var utentiErrorRecord = lte.getUtentiErrorRecord();
//        var error = new UtentiErrorResponse()
//            .errorCode(utentiErrorRecord.getErrorCode())
//            .errorMessage(lte.getMessage());
//        return new ResponseEntity<>(error, utentiErrorRecord.getHttpStatus());
//    }
}
