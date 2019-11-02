package org.miage.intervenantsservice.boundary;

import org.miage.intervenantsservice.entity.Intervenant;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/intervenants", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Intervenant.class)
public class IntervenantRepresentation {

	private final IntervenantResource ir;

	public IntervenantRepresentation(IntervenantResource ir) {
		this.ir = ir;
	}

	// Get all
	@GetMapping("")
	public ResponseEntity<?> getAllIntervenants() {
		Iterable<Intervenant> allIntervenants = ir.findAll();
		return new ResponseEntity<>(IntervenantToResource(allIntervenants), HttpStatus.OK);
	}

	@GetMapping(value = "/{intervenantId}")
	public ResponseEntity<?> getIntervenant(@PathVariable("intervenantId") String id) {
		return Optional.ofNullable(ir.findById(id))
				.filter(Optional::isPresent)
				.map(i -> new ResponseEntity<>(IntervenantToResource(i.get(),true), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// POST
	@PostMapping
	public ResponseEntity<?> saveIntervenant(@RequestBody Intervenant intervenant) {
		intervenant.setId(UUID.randomUUID().toString());
		Intervenant saved = ir.save(intervenant);
		HttpHeaders responsHeaders = new HttpHeaders();
		responsHeaders.setLocation(linkTo(IntervenantRepresentation.class).slash(saved.getId()).toUri());
		return new ResponseEntity<>(null, responsHeaders, HttpStatus.CREATED);
	}

	// PUT
	@PutMapping(value = "/{intervenantId}")
	public ResponseEntity<?> putIntervenant(@RequestBody Intervenant intervenant,
		@PathVariable("intervenantId") String intervenantId) {
		Optional<Intervenant> body = Optional.ofNullable(intervenant);
		if (!body.isPresent()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (!ir.existsById(intervenantId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		intervenant.setId(intervenantId);
		Intervenant res = ir.save(intervenant);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping(value="/{intervenantId}")
	public ResponseEntity<?> deleteIntervenant(@PathVariable String intervenantId){
		Optional<Intervenant> intervenant = ir.findById(intervenantId);
		if(intervenant.isPresent()) {
			ir.delete(intervenant.get());
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/test")
	public String listIntervenants() {
		return "toto, Emilie";
	}

	private Resources<Resource<Intervenant>> IntervenantToResource(Iterable<Intervenant>intervenants) {
		Link selfLInk = linkTo(methodOn(IntervenantRepresentation.class).getAllIntervenants()).withSelfRel();
		List<Resource<Intervenant>> intervenantResources = new ArrayList();
		intervenants.forEach(intervenant -> intervenantResources.add(IntervenantToResource(intervenant, false)));
		return new Resources<>(intervenantResources, selfLInk);
	}

	private Resource<Intervenant> IntervenantToResource(Intervenant intervenant, Boolean collection) {
		Link selfLink = linkTo(IntervenantRepresentation.class)
				.slash(intervenant.getId())
				.withSelfRel();
		if (collection) {
			Link collectionLink = linkTo(methodOn(IntervenantRepresentation.class).getAllIntervenants()).withRel("collection");
			return new Resource<>(intervenant, selfLink, collectionLink);
		} else {
			return new Resource<>(intervenant, selfLink);
		}
	}



}
