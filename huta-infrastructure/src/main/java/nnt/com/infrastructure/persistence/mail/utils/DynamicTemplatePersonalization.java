package nnt.com.infrastructure.persistence.mail.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DynamicTemplatePersonalization extends Personalization {

    @JsonProperty(value = "dynamic_template_data")
    private Map<String, String> dynamic_template_data;

    @JsonProperty("dynamic_template_data")
    public Map<String, Object> getDynamicTemplateData() {
        if (dynamic_template_data == null) {
            return Collections.emptyMap();
        }
        return new HashMap<>(dynamic_template_data);
    }

    public void addDynamicTemplateData(String key, String value) {
        if (dynamic_template_data == null) {
            dynamic_template_data = new HashMap<String, String>();
            dynamic_template_data.put(key, value);
        } else {
            dynamic_template_data.put(key, value);
        }
    }
}
