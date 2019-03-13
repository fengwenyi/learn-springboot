package com.fengwenyi.springbootapireactiveweb;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author Wenyi Feng
 * @since 2019-03-12
 */
@Component
public class UserHandler {

    List<UserModel> list = new ArrayList<>();

    /*public Mono<ServerResponse> getPerson(ServerRequest request) {
        int personId = Integer.valueOf(request.pathVariable("id"));
        Mono<UserModel> personMono = this.repository.getPerson(personId);
        return personMono
                .flatMap(person -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(person)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }*/


    public Mono<ServerResponse> savePerson(ServerRequest request) {

        //get the object and put to list
        Mono<UserModel> model = request.bodyToMono(UserModel.class);
        //final ResultModel[] data = new ResultModel[1];

        model.doOnNext(System.out::println).thenEmpty(Mono.empty());

        //return the result
        //return Mono.just(resultViewModel);

        System.out.println("list size : " + list.size());

        return ServerResponse.ok().build();
    }

    /*public Mono<ServerResponse> allPeople(ServerRequest request) {
        Flux<Person> people = this.repository.allPeople();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(people, Person.class);
    }*/

}
