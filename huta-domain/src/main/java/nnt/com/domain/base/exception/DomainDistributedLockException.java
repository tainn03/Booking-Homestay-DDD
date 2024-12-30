package nnt.com.domain.base.exception;

public class DomainDistributedLockException extends RuntimeException {
    public DomainDistributedLockException(String message) {
        super(message);
    }
}
