package moa.moabackend.global.error;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moa.moabackend.global.error.exception.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

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
            setErrorResponse(response, e);

        } catch (Exception e) {
            response.resetBuffer();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");

            ErrorResponse error =
                    ErrorResponse.of("INTERNAL_SERVER_ERROR", "서버 오류");

            response.getWriter()
                    .write(objectMapper.writeValueAsString(error));
        }
    }

    private void setErrorResponse(HttpServletResponse response, CustomException e)
            throws IOException {

        response.resetBuffer();
        response.setStatus(e.getErrorCode().getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse error = ErrorResponse.of(
                e.getErrorCode().name(),
                e.getErrorCode().getMessage()
        );

        response.getWriter()
                .write(objectMapper.writeValueAsString(error));
    }
}
