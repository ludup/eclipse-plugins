package com.hypersocket.assignable.json;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hypersocket.assignable.TemplateAssignableResource;
import com.hypersocket.assignable.TemplateAssignableResourceColumns;
import com.hypersocket.assignable.TemplateAssignableResourceService;
import com.hypersocket.assignable.TemplateAssignableResourceServiceImpl;
import com.hypersocket.auth.json.AuthenticationRequired;
import com.hypersocket.auth.json.ResourceController;
import com.hypersocket.auth.json.UnauthorizedException;
import com.hypersocket.context.AuthenticatedContext;
import com.hypersocket.i18n.I18N;
import com.hypersocket.json.ResourceList;
import com.hypersocket.json.ResourceStatus;
import com.hypersocket.permissions.AccessDeniedException;
import com.hypersocket.permissions.RoleUtils;
import com.hypersocket.properties.PropertyCategory;
import com.hypersocket.resource.AssignableResourceUpdate;
import com.hypersocket.resource.ResourceException;
import com.hypersocket.resource.ResourceNotFoundException;
import com.hypersocket.session.json.SessionTimeoutException;
import com.hypersocket.tables.Column;
import com.hypersocket.tables.ColumnSort;
import com.hypersocket.tables.BootstrapTableResult;
import com.hypersocket.tables.json.BootstrapTablePageProcessor;

@Controller
public class TemplateAssignableResourceController extends ResourceController {

	/**
	 * TODO rename this class to match your entity.
	 * 
	 * rename RequestMapping annotions for your desired resource URLs. e.g
	 * replace <resources> for example with "applications" <Resources> with "Applications"
	 * <resource> with "application" and <Resource> with "Application"
	 */
	@Autowired
	private TemplateAssignableResourceService resourceService;

	@AuthenticationRequired
	@RequestMapping(value = "<resources>/list", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public ResourceList<TemplateAssignableResource> getResources(HttpServletRequest request,
			HttpServletResponse response) throws AccessDeniedException,
			UnauthorizedException, SessionTimeoutException {

		return new ResourceList<>(resourceService.getResources(sessionUtils.getCurrentRealm(request)));
	}
	
	@AuthenticationRequired
	@RequestMapping(value = "<resources>/my<Resources>", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public ResourceList<TemplateAssignableResource> getResourcesByCurrentPrincipal(
			HttpServletRequest request, HttpServletResponse response)
			throws AccessDeniedException, UnauthorizedException,
			SessionTimeoutException {

		return new ResourceList<>(resourceService.getResources(sessionUtils.getPrincipal(request)));
	}

	@AuthenticationRequired
	@RequestMapping(value = "<resources>/table", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public BootstrapTableResult<?> tableResources(
			final HttpServletRequest request, HttpServletResponse response)
			throws AccessDeniedException, UnauthorizedException,
			SessionTimeoutException {

		return processDataTablesRequest(request,
				new BootstrapTablePageProcessor() {

					@Override
					public Column getColumn(String col) {
						return TemplateAssignableResourceColumns.valueOf(col.toUpperCase());
					}

					@Override
					public List<?> getPage(String searchColumn, String searchPattern, int start,
							int length, ColumnSort[] sorting)
							throws UnauthorizedException,
							AccessDeniedException {
						return resourceService.searchResources(
								sessionUtils.getCurrentRealm(request),
								searchColumn, searchPattern, start, length, sorting);
					}

					@Override
					public Long getTotalCount(String searchColumn, String searchPattern)
							throws UnauthorizedException,
							AccessDeniedException {
						return resourceService.getResourceCount(
								sessionUtils.getCurrentRealm(request),
								searchColumn, searchPattern);
					}
				});
	}

	
	@AuthenticationRequired
	@RequestMapping(value = "<resources>/template", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public ResourceList<PropertyCategory> getResourceTemplate(
			HttpServletRequest request) throws AccessDeniedException,
			UnauthorizedException, SessionTimeoutException {
		return new ResourceList<>(resourceService.getPropertyTemplate());
	}
	
	@AuthenticationRequired
	@RequestMapping(value = "<resources>/properties/{id}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public ResourceList<PropertyCategory> getActionTemplate(
			HttpServletRequest request, @PathVariable Long id)
			throws AccessDeniedException, UnauthorizedException,
			SessionTimeoutException, ResourceNotFoundException {
		return new ResourceList<PropertyCategory>(resourceService.getPropertyTemplate(resourceService.getResourceById(id)));
	}

	@AuthenticationRequired
	@RequestMapping(value = "<resources>/<resource>/{id}", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public TemplateAssignableResource getResource(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("id") Long id)
			throws AccessDeniedException, UnauthorizedException,
			ResourceNotFoundException, SessionTimeoutException {

		return resourceService.getResourceById(id);
	}

	@AuthenticationRequired
	@RequestMapping(value = "<resources>/<resource>", method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public ResourceStatus<TemplateAssignableResource> createOrUpdateResource(
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody AssignableResourceUpdate resource)
			throws AccessDeniedException, UnauthorizedException,
			SessionTimeoutException {

		try {
			TemplateAssignableResource newResource;

			var realm = sessionUtils.getCurrentRealm(request);

			var roles = RoleUtils.processPermissions(resource.getRoles());
			
			var properties = Stream.of(resource.getProperties()).collect(
					Collectors.toMap(i -> i.getId(), i -> i.getValue()));

			if (resource.getId() != null) {
				newResource = resourceService.updateResource(
						resourceService.getResourceById(resource.getId()),
						resource.getName(), roles, properties);
			} else {
				newResource = resourceService.createResource(
						resource.getName(), roles,
						realm, properties);
			}
			return new ResourceStatus<>(newResource,
					I18N.getResource(sessionUtils.getLocale(request),
							TemplateAssignableResourceServiceImpl.RESOURCE_BUNDLE,
							resource.getId() != null ? "resource.updated.info"
									: "resource.created.info", resource
									.getName()));

		} catch (ResourceException e) {
			return new ResourceStatus<>(false,
					e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@AuthenticationRequired
	@RequestMapping(value = "<resources>/<resource>/{id}", method = RequestMethod.DELETE, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public ResourceStatus<TemplateAssignableResource> deleteResource(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Long id) throws AccessDeniedException,
			UnauthorizedException, SessionTimeoutException {

		try {
			var resource = resourceService.getResourceById(id);

			if (resource == null) {
				return new ResourceStatus<>(false,
						I18N.getResource(sessionUtils.getLocale(request),
								TemplateAssignableResourceServiceImpl.RESOURCE_BUNDLE,
								"error.invalidResourceId", id));
			}

			var preDeletedName = resource.getName();
			resourceService.deleteResource(resource);

			return new ResourceStatus<>(true, I18N.getResource(
					sessionUtils.getLocale(request),
					TemplateAssignableResourceServiceImpl.RESOURCE_BUNDLE,
					"resource.deleted.info", preDeletedName));

		} catch (ResourceException e) {
			return new ResourceStatus<>(false, e.getMessage());
		}
	}
	
	@AuthenticationRequired
	@RequestMapping(value = "<resources>/personal", method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@AuthenticatedContext
	public BootstrapTableResult<?> personalResources(final HttpServletRequest request,
			HttpServletResponse response) throws AccessDeniedException,
			UnauthorizedException, SessionTimeoutException {

		return processDataTablesRequest(request,
				new BootstrapTablePageProcessor() {

					@Override
					public Column getColumn(String col) {
						return TemplateAssignableResourceColumns.valueOf(col.toUpperCase());
					}

					@Override
					public Collection<?> getPage(String searchColumn, String searchPattern, int start, int length,
							ColumnSort[] sorting) throws UnauthorizedException, AccessDeniedException {
						return resourceService.searchPersonalResources(sessionUtils.getPrincipal(request),
								searchColumn, searchPattern, start, length, sorting);
					}
					
					@Override
					public Long getTotalCount(String searchColumn, String searchPattern) throws UnauthorizedException, AccessDeniedException {
						return resourceService.getPersonalResourceCount(
								sessionUtils.getPrincipal(request),
								searchColumn, searchPattern);
					}
				});
	}
}
