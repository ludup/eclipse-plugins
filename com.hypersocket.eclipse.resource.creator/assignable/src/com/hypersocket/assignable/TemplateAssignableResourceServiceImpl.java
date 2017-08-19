package com.hypersocket.assignable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypersocket.assignable.events.TemplateAssignableResourceCreatedEvent;
import com.hypersocket.assignable.events.TemplateAssignableResourceDeletedEvent;
import com.hypersocket.assignable.events.TemplateAssignableResourceEvent;
import com.hypersocket.assignable.events.TemplateAssignableResourceUpdatedEvent;
import com.hypersocket.events.EventService;
import com.hypersocket.i18n.I18NService;
import com.hypersocket.menus.MenuRegistration;
import com.hypersocket.menus.MenuService;
import com.hypersocket.permissions.AccessDeniedException;
import com.hypersocket.permissions.PermissionCategory;
import com.hypersocket.permissions.PermissionService;
import com.hypersocket.permissions.Role;
import com.hypersocket.properties.EntityResourcePropertyStore;
import com.hypersocket.properties.PropertyCategory;
import com.hypersocket.realm.Realm;
import com.hypersocket.resource.AbstractAssignableResourceRepository;
import com.hypersocket.resource.AbstractAssignableResourceServiceImpl;
import com.hypersocket.resource.ResourceChangeException;
import com.hypersocket.resource.ResourceException;

@Service
public class TemplateAssignableResourceServiceImpl extends
		AbstractAssignableResourceServiceImpl<TemplateAssignableResource>implements TemplateAssignableResourceService {

	/**
	 * TODO rename this class to match your entity.
	 * 
	 * Set the correct resource bundle and create your bundle in
	 * src/main/resources/i18n
	 */
	public static final String RESOURCE_BUNDLE = "<Resource>ResourceService";

	@Autowired
	TemplateAssignableResourceRepository repository;

	@Autowired
	I18NService i18nService;

	@Autowired
	PermissionService permissionService;

	@Autowired
	MenuService menuService;

	@Autowired
	EventService eventService;

	public TemplateAssignableResourceServiceImpl() {
		super("<resource>");
	}

	@PostConstruct
	private void postConstruct() {

		i18nService.registerBundle(RESOURCE_BUNDLE);

		PermissionCategory cat = permissionService.registerPermissionCategory(RESOURCE_BUNDLE, "category.<resource>");

		for (TemplateAssignableResourcePermission p : TemplateAssignableResourcePermission.values()) {
			permissionService.registerPermission(p, cat);
		}

		/**
		 * TODO change the property template xml file
		 */
		repository.loadPropertyTemplates("<resource>ResourceTemplate.xml");

		/**
		 * TODO add your menu item and other initialization.
		 */
		menuService.registerMenu(
				new MenuRegistration(RESOURCE_BUNDLE, "<resources>", "<resourceIcon>", "<resources>", 100,
						TemplateAssignableResourcePermission.READ, TemplateAssignableResourcePermission.CREATE,
						TemplateAssignableResourcePermission.UPDATE, TemplateAssignableResourcePermission.DELETE),
				MenuService.MENU_RESOURCES);
		
		menuService.registerMenu(
				new MenuRegistration(RESOURCE_BUNDLE, "my<Resources>", "<resourceIcon>", "my<Resources>", 100,
						null, null, null, null),
				MenuService.MENU_MY_RESOURCES);

		/**
		 * Register the events. All events have to be registerd so the system
		 * knows about them.
		 */
		eventService.registerEvent(TemplateAssignableResourceEvent.class, RESOURCE_BUNDLE);
		eventService.registerEvent(TemplateAssignableResourceCreatedEvent.class, RESOURCE_BUNDLE);
		eventService.registerEvent(TemplateAssignableResourceUpdatedEvent.class, RESOURCE_BUNDLE);
		eventService.registerEvent(TemplateAssignableResourceDeletedEvent.class, RESOURCE_BUNDLE);

		EntityResourcePropertyStore.registerResourceService(TemplateAssignableResource.class, repository);

	}

	@Override
	protected AbstractAssignableResourceRepository<TemplateAssignableResource> getRepository() {
		return repository;
	}

	@Override
	protected String getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	@Override
	public Class<?> getPermissionType() {
		return TemplateAssignableResourcePermission.class;
	}

	@Override
	protected void fireResourceCreationEvent(TemplateAssignableResource resource) {
		eventService.publishEvent(new TemplateAssignableResourceCreatedEvent(this, getCurrentSession(), resource));
	}

	@Override
	protected void fireResourceCreationEvent(TemplateAssignableResource resource, Throwable t) {
		eventService.publishEvent(new TemplateAssignableResourceCreatedEvent(this, resource, t, getCurrentSession()));
	}

	@Override
	protected void fireResourceUpdateEvent(TemplateAssignableResource resource) {
		eventService.publishEvent(new TemplateAssignableResourceUpdatedEvent(this, getCurrentSession(), resource));
	}

	@Override
	protected void fireResourceUpdateEvent(TemplateAssignableResource resource, Throwable t) {
		eventService.publishEvent(new TemplateAssignableResourceUpdatedEvent(this, resource, t, getCurrentSession()));
	}

	@Override
	protected void fireResourceDeletionEvent(TemplateAssignableResource resource) {
		eventService.publishEvent(new TemplateAssignableResourceDeletedEvent(this, getCurrentSession(), resource));
	}

	@Override
	protected void fireResourceDeletionEvent(TemplateAssignableResource resource, Throwable t) {
		eventService.publishEvent(new TemplateAssignableResourceDeletedEvent(this, resource, t, getCurrentSession()));
	}

	@Override
	public TemplateAssignableResource updateResource(TemplateAssignableResource resource, String name, Set<Role> roles,
			Map<String, String> properties) throws ResourceException, AccessDeniedException {

		resource.setName(name);

		/**
		 * Set any additional fields on your resource here before calling
		 * updateResource.
		 * 
		 * Remember to fill in the fire*Event methods to ensure events are fired
		 * for all operations.
		 */
		updateResource(resource, roles, properties);

		return resource;
	}

	@Override
	public TemplateAssignableResource createResource(String name, Set<Role> roles, Realm realm,
			Map<String, String> properties) throws ResourceException, AccessDeniedException {

		TemplateAssignableResource resource = new TemplateAssignableResource();
		resource.setName(name);
		resource.setRealm(realm);
		resource.setRoles(roles);

		/**
		 * Set any additional fields on your resource here before calling
		 * createResource.
		 * 
		 * Remember to fill in the fire*Event methods to ensure events are fired
		 * for all operations.
		 */
		createResource(resource, properties);

		return resource;
	}

	@Override
	public Collection<PropertyCategory> getPropertyTemplate(TemplateAssignableResource resource)
			throws AccessDeniedException {

		assertPermission(TemplateAssignableResourcePermission.READ);
		return repository.getPropertyCategories(resource);
	}

	@Override
	public Collection<PropertyCategory> getPropertyTemplate() throws AccessDeniedException {
		assertPermission(TemplateAssignableResourcePermission.READ);
		return repository.getPropertyCategories(null);
	}

	@Override
	protected Class<TemplateAssignableResource> getResourceClass() {
		return TemplateAssignableResource.class;
	}

}
