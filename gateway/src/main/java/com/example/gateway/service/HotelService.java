package com.example.gateway.service;

import com.example.gateway.dto.PaginationResponseDTO;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class HotelService {

    @Value("${reservation.service.url}")
    private String basicUrl;
    public PaginationResponseDTO getHotels(int page, int row) throws URISyntaxException {
        System.out.println(" Stigao na gateway");
        URI uri = new URI(this.basicUrl.toString() + "/hotels/"+page+"/"+row);
        System.out.println(uri.getPath());
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<PaginationResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, PaginationResponseDTO.class);
        System.out.println(result.getBody());
        System.out.println(result);
        return result.getBody();
    }

}
