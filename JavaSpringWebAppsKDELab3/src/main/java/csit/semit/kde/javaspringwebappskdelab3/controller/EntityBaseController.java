package csit.semit.kde.javaspringwebappskdelab3.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Abstract base controller for handling common entity-related web requests.
 * <p>
 * This controller provides common methods for handling CRUD operations for entities.
 * It defines abstract methods that must be implemented by subclasses to specify the base path and entity name.
 * </p>
 * <p>
 * The main functionalities provided by this controller include:
 * <ul>
 *   <li>Fetching an entity by its ID and adding it to the model.</li>
 *   <li>Creating a new entity and adding it to the model.</li>
 *   <li>Listing all entities and adding them to the model.</li>
 * </ul>
 * </p>
 * <p>
 * This controller uses Spring MVC annotations to map requests and handle them appropriately.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Controller
 * @RequestMapping("/api/entities")
 * public class SomeEntityController extends EntityBaseController {
 *     // methods and logic
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link Model}</li>
 *   <li>{@link GetMapping}</li>
 *   <li>{@link PathVariable}</li>
 *   <li>{@link RequestParam}</li>
 * </ul>
 * </p>
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@link GetMapping}</li>
 *   <li>{@link PathVariable}</li>
 *   <li>{@link RequestParam}</li>
 * </ul>
 * </p>
 * <p>
 * Note: This controller assumes that the base path for API endpoints is specified by the subclass.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see Model
 * @see GetMapping
 * @see PathVariable
 * @see RequestParam
 * @since 1.0.0
 */
public abstract class EntityBaseController {
    protected abstract String getBasePath();

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        return getBasePath() + "/" + getEntityName();
    }

    @GetMapping("/create")
    public String create(@RequestParam(required = false) String trainNumber, Model model) {
        return getBasePath() + "/" + getEntityName() + "-create";
    }

    @GetMapping
    public String list(Model model) {
        return getBasePath() + "/" + getEntityName() + "s";
    }

    protected abstract String getEntityName();
}