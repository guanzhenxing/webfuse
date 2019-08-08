package cn.webfuse.framework.exception.handle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Restful错误
 *
 * @author Jesen
 */
@Getter
@AllArgsConstructor
public class RestfulError {

    private final HttpStatus status;
    private final String code;
    private final String message;
    private final String developerMessage;
    private final Throwable throwable;
    private final Date serverTime = new Date();
    private String requestId;
    private String hostId;
    private String document;


    public static class Builder {
        private HttpStatus status;
        private String code;
        private String message;
        private String developerMessage;
        private Throwable throwable;
        private String requestId;
        private String hostId;
        private String document;

        public Builder() {
        }

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public Builder setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public void setHostId(String hostId) {
            this.hostId = hostId;
        }

        public void setDocument(String document) {
            this.document = document;
        }

        public RestfulError build() {
            if (this.status == null) {
                this.status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return new RestfulError(
                    this.status, this.code, this.message, this.developerMessage, this.throwable,
                    this.requestId, this.hostId, this.document
            );
        }

    }
}
