package moa.moabackend.global.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moa.moabackend.global.error.exception.CustomException;
import moa.moabackend.global.error.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (CustomException e) {
            setErrorResponse(response, e.getErrorCode());

        } catch (Exception e) {
            setErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {

        response.resetBuffer();
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse error = ErrorResponse.of(
                errorCode.name(),
                errorCode.getMessage()
        );

        response.getWriter()
                .write(objectMapper.writeValueAsString(error));
    }
}
