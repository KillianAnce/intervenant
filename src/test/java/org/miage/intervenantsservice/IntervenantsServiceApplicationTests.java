package org.miage.intervenantsservice;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.miage.intervenantsservice.boundary.IntervenantResource;
import org.miage.intervenantsservice.entity.Intervenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntervenantsServiceApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	IntervenantResource ir;

	@Before
	public void setUpContext() {
		ir.deleteAll();
	}

	@Test
	public void getOne() {
		Intervenant i1 = new Intervenant("Tom", "Swayer", "US", "Amérique");
		i1.setId(UUID.randomUUID().toString());
		ir.save(i1);

		ResponseEntity<String> response = restTemplate.getForEntity("/intervenants/" +
				i1.getId(), String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Tom");
	}

	@Test
	public void getAll() {
		Intervenant i1 = new Intervenant("Tom", "Swayer", "US", "Amérique");
		i1.setId(UUID.randomUUID().toString());
		ir.save(i1);
		Intervenant i2 = new Intervenant("wow", "blabla", "Nancy", "54000");
		i2.setId(UUID.randomUUID().toString());
		ir.save(i2);
		ResponseEntity<String> response = restTemplate.getForEntity("/intervenants/", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("Tom","wow");
	}

	//post
	@Test
	public void post() {
		Intervenant intervenant = new Intervenant("Tom", "Swayer", "US", "Amérique");
		intervenant.setId(UUID.randomUUID().toString());

		ResponseEntity<String> response = restTemplate.postForEntity("/intervenants", intervenant , String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	//put
	@Test
	public void put() {
		HttpHeaders headers = new HttpHeaders();

		Intervenant intervenant = new Intervenant("Tom", "Swayer", "US", "Amérique");
		intervenant.setId(UUID.randomUUID().toString());
		ir.save(intervenant);
		final String url = String.format("/intervenants/" + intervenant.getId());

		intervenant.setNom("JeanJean");

		HttpEntity<Intervenant> requestEntity = new HttpEntity<Intervenant>(intervenant, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Intervenant i2 = ir.findById(intervenant.getId()).get();
		assertThat(i2.getNom().contains("JeanJean")).isTrue();
	}

	//delete
	@Test
	public void delete() {
		HttpHeaders headers = new HttpHeaders();

		Intervenant intervenant = new Intervenant("Tom", "Swayer", "US", "Amérique");
		intervenant.setId(UUID.randomUUID().toString());
		ir.save(intervenant);
		final String url = String.format("/intervenants/" + intervenant.getId());

		HttpEntity<Intervenant> requestEntity = new HttpEntity<Intervenant>(intervenant, headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		assertThat(ir.findById(intervenant.getId())).isEmpty();

	}
}
