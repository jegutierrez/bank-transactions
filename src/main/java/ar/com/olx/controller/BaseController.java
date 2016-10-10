package ar.com.olx.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jegutierrez on 09/10/16.
 */
public class BaseController {

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Map<String, String>> handleNoResultException(
            NoResultException noResultException, HttpServletRequest request) {

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", noResultException.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(
            Exception exception, HttpServletRequest request) {

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error", exception.getMessage());

        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
