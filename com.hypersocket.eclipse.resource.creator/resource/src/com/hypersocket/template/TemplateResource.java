package com.hypersocket.template;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.hypersocket.realm.Realm;
import com.hypersocket.resource.RealmResource;

@Entity
@Table(name="<resource>_resource")
@SuppressWarnings("serial")
public class TemplateResource extends RealmResource {

	
	@ManyToOne
	@JoinColumn(name = "realm_id", foreignKey = @ForeignKey(name = "<resource>_resource_cascade_1"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	protected Realm realm;

	@Override
	protected Realm doGetRealm() {
		return realm;
	}

	@Override
	public void setRealm(Realm realm) {
		this.realm = realm;
	}
	
	/**
	 * TODO Add any further fields your resource requires.
	 */
}
