package com.example.demo.common.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class CommonErrorViewResolver implements ErrorViewResolver {

	private final ApplicationContext applicationContext;
	private final TemplateAvailabilityProviders templateAvailabilityProviders;

	public CommonErrorViewResolver(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.templateAvailabilityProviders = new TemplateAvailabilityProviders(applicationContext);
	}

	/**
	 * 
	 */
	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		var type = request.getParameter("type");

		// TODO セキュリティホールにならないように、実際には有効な type の判定が必要
		String errorViewName = "error/" + type;
		TemplateAvailabilityProvider provider = this.templateAvailabilityProviders.getProvider(errorViewName,
				this.applicationContext);
		if (provider != null) {
			return new ModelAndView(errorViewName, model);
		}

		return null;
	}

}
