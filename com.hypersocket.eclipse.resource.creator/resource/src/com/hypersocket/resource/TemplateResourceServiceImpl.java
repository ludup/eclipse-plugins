package com.hypersocket.resource;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hypersocket.events.EventService;
import com.hypersocket.i18n.I18NService;
import com.hypersocket.menus.MenuRegistration;
import com.hypersocket.menus.MenuService;
import com.hypersocket.permissions.AccessDeniedException;
import com.hypersocket.permissions.PermissionCategory;
import com.hypersocket.permissions.PermissionService;
import com.hypersocket.properties.PropertyCategory;
import com.hypersocket.realm.Realm;
import com.hypersocket.resource.events.TemplateResourceCreatedEvent;
import com.hypersocket.resource.events.TemplateResourceDeletedEvent;
import com.hypersocket.resource.events.TemplateResourceEvent;
import com.hypersocket.resource.events.TemplateResourceUpdatedEvent;

@Service
public class TemplateResourceServiceImpl extends
		AbstractResourceServiceImpl<TemplateResource> implements
		TemplateResourceService {

	public static final String RESOURCE_BUNDLE = "<Resource>ResourceService";

	@Autowired
	TemplateResourceRepository repository;

	@Autowired
	I18NService i18nService;

	@Autowired
	PermissionService permissionService;

	@Autowired
	MenuService menuService;

	@Autowired
	EventService eventService;

	public TemplateResourceServiceImpl() {
		super("<Resource>");
	}
	
	@PostConstruct
	private void postConstruct() {

		i18nService.registerBundle(RESOURCE_BUNDLE);

		PermissionCategory cat = permissionService.registerPermissionCategory(
				RESOURCE_BUNDLE, "category.<resources>");

		for (TemplateResourcePermission p : TemplateResourcePermission.values()) {
			permissionService.registerPermission(p, cat);
		}

		repository.loadPropertyTemplates("<resource>ResourceTemplate.xml");

		menuService.registerMenu(new MenuRegistration(RESOURCE_BUNDLE,
				"<resources>", "<resourceIcon>", "<resources>", 100,
				TemplateResourcePermission.READ,
				TemplateResourcePermission.CREATE,
				TemplateResourcePermission.UPDATE,
				TemplateResourcePermission.DELETE), MenuService.MENU_RESOURCES);

		/**
		 * Register the events. All events have to be registerd so the system
		 * knows about them.
		 */
		eventService.registerEvent(
				TemplateResourceEvent.class, RESOURCE_BUNDLE,
				this);
		eventService.registerEvent(
				TemplateResourceCreatedEvent.class, RESOURCE_BUNDLE,
				this);
		eventService.registerEvent(
				TemplateResourceUpdatedEvent.class, RESOURCE_BUNDLE,
				this);
		eventService.registerEvent(
				TemplateResourceDeletedEvent.class, RESOURCE_BUNDLE,
				this);

		repository.getEntityStore().registerResourceService(TemplateResource.class, repository);
	}

	@Override
	protected AbstractResourceRepository<TemplateResource> getRepository() {
		return repository;
	}

	@Override
	protected String getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	@Override
	public Class<TemplateResourcePermission> getPermissionType() {
		return TemplateResourcePermission.class;
	}
	
	protected Class<TemplateResource> getResourceClass() {
		return TemplateResource.class;
	}
	
	@Override
	protected void fireResourceCreationEvent(TemplateResource resource) {
		eventService.publishEvent(new TemplateResourceCreatedEvent(this,
				getCurrentSession(), resource));
	}

	@Override
	protected void fireResourceCreationEvent(TemplateResource resource,
			Throwable t) {
		eventService.publishEvent(new TemplateResourceCreatedEvent(this,
				resource, t, getCurrentSession()));
	}

	@Override
	protected void fireResourceUpdateEvent(TemplateResource resource) {
		eventService.publishEvent(new TemplateResourceUpdatedEvent(this,
				getCurrentSession(), resource));
	}

	@Override
	protected void fireResourceUpdateEvent(TemplateResource resource,
			Throwable t) {
		eventService.publishEvent(new TemplateResourceUpdatedEvent(this,
				resource, t, getCurrentSession()));
	}

	@Override
	protected void fireResourceDeletionEvent(TemplateResource resource) {
		eventService.publishEvent(new TemplateResourceDeletedEvent(this,
				getCurrentSession(), resource));
	}

	@Override
	protected void fireResourceDeletionEvent(TemplateResource resource,
			Throwable t) {
		eventService.publishEvent(new TemplateResourceDeletedEvent(this,
				resource, t, getCurrentSession()));
	}

	@Override
	public TemplateResource updateResource(TemplateResource resource,
			String name, Map<String, String> properties)
			throws ResourceChangeException, AccessDeniedException {

		resource.setName(name);

		/**
		 * Set any additional fields on your resource here before calling
		 * updateResource.
		 * 
		 * Remember to fill in the fire*Event methods to ensure events are fired
		 * for all operations.
		 */
		updateResource(resource, properties);

		return resource;
	}

	@Override
	public TemplateResource createResource(String name, Realm realm,
			Map<String, String> properties) throws ResourceCreationException,
			AccessDeniedException {

		TemplateResource resource = new TemplateResource();
		resource.setName(name);
		resource.setRealm(realm);
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
	public Collection<PropertyCategory> getPropertyTemplate()
			throws AccessDeniedException {

		assertPermission(TemplateResourcePermission.READ);

		return repository.getPropertyCategories(null);
	}

	@Override
	public Collection<PropertyCategory> getPropertyTemplate(
			TemplateResource resource) throws AccessDeniedException {

		assertPermission(TemplateResourcePermission.READ);

		return repository.getPropertyCategories(resource);
	}

}
