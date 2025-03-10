package br.com.personal.observableApp.infra.exception;

import br.com.personal.observableApp.infra.exception.dto.RequestErrorDto;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {

    private final Counter userErrors;

    public GlobalException(MeterRegistry registry) {
        this.userErrors = Counter.builder("USER_ERRORS")
                .description("Error on user job...")
                .register(registry);
    }

    @ResponseBody
    @ExceptionHandler(RequestErrorException.class)
    public ResponseEntity<RequestErrorDto> handler(RequestErrorException ex) {
        userErrors.increment();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new RequestErrorDto(ex.getMessage()));
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RequestErrorDto> handler(UserNotFoundException ex) {
        userErrors.increment();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RequestErrorDto(ex.getMessage()));
    }
}
