package csit.semit.kde.javaspringwebappskdelab3.controller;

import csit.semit.kde.javaspringwebappskdelab3.dto.Identifiable;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResultHandler;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Abstract base REST controller for handling common entity-related web requests.
 * <p>
 * This controller provides common methods for handling CRUD operations for entities.
 * It defines abstract methods that must be implemented by subclasses to specify the base path and entity-specific operations.
 * </p>
 * <p>
 * The main functionalities provided by this controller include:
 * <ul>
 *   <li>Fetching an entity by its ID and returning it in the response.</li>
 *   <li>Creating a new entity and returning the created entity in the response.</li>
 *   <li>Deleting an entity by its ID and returning a success message in the response.</li>
 * </ul>
 * </p>
 * <p>
 * This controller uses Spring MVC annotations to map requests and handle them appropriately.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @RestController
 * @RequestMapping("/api/entities")
 * public class SomeEntityRestController extends EntityBaseRestController<SomeEntityDTO> {
 *     // methods and logic
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link Identifiable}</li>
 *   <li>{@link ServiceResult}</li>
 *   <li>{@link ServiceResultHandler}</li>
 *   <li>{@link ServiceStatus}</li>
 *   <li>{@link HttpServletRequest}</li>
 *   <li>{@link HttpHeaders}</li>
 *   <li>{@link ResponseEntity}</li>
 *   <li>{@link ServletUriComponentsBuilder}</li>
 * </ul>
 * </p>
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@link RestController}</li>
 *   <li>{@link RequestMapping}</li>
 *   <li>{@link GetMapping}</li>
 *   <li>{@link PostMapping}</li>
 *   <li>{@link DeleteMapping}</li>
 *   <li>{@link PathVariable}</li>
 *   <li>{@link RequestBody}</li>
 * </ul>
 * </p>
 * <p>
 * Note: This controller assumes that the base path for API endpoints is specified by the subclass.
 * </p>
 * <p>
 * Error handling:
 * <ul>
 *   <li>Handles service result statuses and responds with appropriate HTTP status codes and messages.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see Identifiable
 * @see ServiceResult
 * @see ServiceResultHandler
 * @see ServiceStatus
 * @see HttpServletRequest
 * @see HttpHeaders
 * @see ResponseEntity
 * @see ServletUriComponentsBuilder
 * @see RestController
 * @see RequestMapping
 * @see GetMapping
 * @see PostMapping
 * @see DeleteMapping
 * @see PathVariable
 * @see RequestBody
 * @since 1.0.0
 */
public abstract class EntityBaseRestController<T extends Identifiable> {
    protected abstract ServiceResult<T> findById(Long id);

    protected abstract ServiceResult<T> saveEntity(T dto);

    protected abstract ServiceResult<Void> deleteEntity(Long id);

    protected abstract String getBasePath();

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ServiceResult<T> result = findById(id);
        return ServiceResultHandler.handleServiceResult(result);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody T dto, HttpServletRequest request) {
        ServiceResult<T> result = saveEntity(dto);

        if (result.getStatus() == ServiceStatus.SUCCESS) {
            URI location = ServletUriComponentsBuilder
                    .fromContextPath(request)
                    .path(getBasePath() + "/{id}")
                    .buildAndExpand(result.getEntity().getId())
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Success-Message", "Resource created successfully");

            return ResponseEntity.created(location)
                    .headers(headers)
                    .body(result.getEntity());
        }

        return ServiceResultHandler.handleServiceResult(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        ServiceResult<Void> result = deleteEntity(id);

        if (result.getStatus() == ServiceStatus.SUCCESS) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Success-Message", "Resource deleted successfully");
            return ResponseEntity.ok().headers(headers).build();
        }

        return ServiceResultHandler.handleServiceResult(result);
    }
}