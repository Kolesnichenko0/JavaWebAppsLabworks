package csit.semit.kde.javaspringwebappskdelab3.controller.train;

import csit.semit.kde.javaspringwebappskdelab3.controller.EntityBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling train-related requests.
 * <p>
 * This controller is mapped to the "/trains" URL and provides methods for handling requests related to trains.
 * It extends the {@link EntityBaseController} to inherit common functionality for entity controllers.
 * </p>
 * <p>
 * The main functionalities provided by this controller include:
 * <ul>
 *   <li>Returning the base path for train-related views.</li>
 *   <li>Returning the entity name for train-related operations.</li>
 *   <li>Handling requests to view tickets for a specific train.</li>
 * </ul>
 * </p>
 * <p>
 * The {@code trainTickets} method handles GET requests to "/trains/{id}/tickets" and adds attributes to the model
 * to indicate whether the user is logged in and whether the request is for a specific train.
 * </p>
 * <p>
 * This controller uses Spring MVC annotations to map requests and handle them appropriately.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see EntityBaseController
 * @see org.springframework.stereotype.Controller
 * @see org.springframework.ui.Model
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @since 1.0.0
 */
@Controller
@RequestMapping("/trains")
public class TrainControllerEntity extends EntityBaseController {
    @Override
    protected String getBasePath() {
        return "train";
    }

    @Override
    protected String getEntityName() {
        return "train";
    }

    @GetMapping("/{id}/tickets")
    public String trainTickets(Model model) {
        boolean isForSpecificTrain = true;
        model.addAttribute("isForSpecificTrain", isForSpecificTrain);
        return "train-ticket/train-tickets";
    }
}