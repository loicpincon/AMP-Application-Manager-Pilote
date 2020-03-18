package organisation.application.manager.pilote.datasource.mongo.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import organisation.apimanager.annotations.ApiManager;
import organisation.application.manager.pilote.commun.controller.DefaultController;

@RestController
@RequestMapping("/datasource/mongo")
@ApiManager("mongo.base")
public class MongoBaseController extends DefaultController {

}
