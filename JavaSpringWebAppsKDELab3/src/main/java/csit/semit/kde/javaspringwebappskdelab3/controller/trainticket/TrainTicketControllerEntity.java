package csit.semit.kde.javaspringwebappskdelab3.controller.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.controller.EntityBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling train ticket-related requests.
 * <p>
 * This controller is mapped to the "/train-tickets" URL and provides methods for handling requests related to train tickets.
 * It extends the {@link EntityBaseController} to inherit common functionality for entity controllers.
 * </p>
 * <p>
 * The main functionalities provided by this controller include:
 * <ul>
 *   <li>Returning the base path for train ticket-related views.</li>
 *   <li>Returning the entity name for train ticket-related operations.</li>
 *   <li>Handling requests to list train tickets.</li>
 *   <li>Handling requests to create a new train ticket.</li>
 * </ul>
 * </p>
 * <p>
 * The {@code list} method handles GET requests to "/train-tickets" and adds attributes to the model
 * to indicate whether the request is for a specific train.
 * </p>
 * <p>
 * The {@code create} method handles GET requests to "/train-tickets/create" and adds attributes to the model
 * to include the train number if provided.
 * </p>
 * <p>
 * This controller uses Spring MVC annotations to map requests and handle them appropriately.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Controller
 * @RequestMapping("/train-tickets")
 * public class TrainTicketControllerEntity extends EntityBaseController {
 *     // methods and logic
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Note: This controller assumes that the base path and entity name are "train-ticket".
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link EntityBaseController}</li>
 *   <li>{@link Model}</li>
 *   <li>{@link GetMapping}</li>
 *   <li>{@link RequestMapping}</li>
 *   <li>{@link RequestParam}</li>
 * </ul>
 * </p>
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@link Controller}</li>
 *   <li>{@link RequestMapping}</li>
 *   <li>{@link GetMapping}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see EntityBaseController
 * @see Model
 * @see GetMapping
 * @see RequestMapping
 * @see RequestParam
 * @see Controller
 * @since 1.0.0
 */
@Controller
@RequestMapping("/train-tickets")
public class TrainTicketControllerEntity extends EntityBaseController {
    @Override
    protected String getBasePath() {
        return "train-ticket";
    }

    @Override
    protected String getEntityName() {
        return "train-ticket";
    }

    @Override
    public String list(Model model) {
        boolean isForSpecificTrain = false;
        model.addAttribute("isForSpecificTrain", isForSpecificTrain);
        return super.list(model);
    }

    @GetMapping("/create")
    @Override
    public String create(@RequestParam(required = false) String trainNumber, Model model) {
        model.addAttribute("trainNumber", trainNumber);
        return super.create(null, model);
    }
}