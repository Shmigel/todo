package com.shmigel.todo.utils;

import com.shmigel.todo.model.UserAuthDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URI;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class Utils {

    /**
     * Creates self link to some resource base on resource id and controller
     * @param controller is class annotated {@link org.springframework.web.bind.annotation.RestController}
     * @param id identifier of the resource
     * @return link to resource
     */
    public static URI selfLink(Class<?> controller, UUID id) {
        return URI.create(linkTo(controller)
                .slash(id).withSelfRel().getHref());
    }

    /**
     * Return authenticated user
     * @return authenticated user
     */
    public static Authentication authenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
