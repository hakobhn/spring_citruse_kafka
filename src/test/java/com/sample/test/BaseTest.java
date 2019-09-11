package com.sample.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.payload.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Resource
    protected WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    ObjectMapper jsonObjectMapper;


    public void setUp() {
        DefaultMockMvcBuilder builders = MockMvcBuilders.webAppContextSetup(webApplicationContext);

        mockMvc = builders.build();
        jsonObjectMapper = new ObjectMapper();

    }


    public ApiResponse getResponseObject(final MvcResult mvcResult) throws Exception {

        if(mvcResult.getResponse().getContentAsString().contains("success")) {
            Object obj = jsonObjectMapper.readValue(
                    mvcResult.getResponse().getContentAsString(), ApiResponse.class
            );
            return jsonObjectMapper.readValue(
                    mvcResult.getResponse().getContentAsString(),
                    new TypeReference<ApiResponse>() {}
                    );
        } else {
            return jsonObjectMapper.readValue(
                    mvcResult.getResponse().getContentAsString(),
                    new TypeReference<ApiResponse>() {}
                    );
        }
    }

    //  GET method call. Request contains parameters as path variables
    public MvcResult performRestCallGET(String context) throws Exception {

        MockHttpServletRequestBuilder builder = get(context);

        return mockMvc.perform(builder).andExpect(status().isOk()).andReturn();
    }

    //  POST method call
    public MvcResult performRestCallPOST(String context, Object jsonObj) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
        logger.info(jsonInString);

        MockHttpServletRequestBuilder builder = post(context);

        builder.contentType(MediaType.APPLICATION_JSON)
                .content(jsonInString);

        return mockMvc.perform(builder).andExpect(status().isOk()).andReturn();
    }

}
