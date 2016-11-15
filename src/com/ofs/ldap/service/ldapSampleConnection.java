package com.ofs.ldap.service;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;

import com.ofs.ldap.api.AppException;

public class ldapSampleConnection {

	public DirContext getConnection() {

		try  {

			String url = "ldap://localhost:10389/ou=users,ou=system";
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, url);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
			env.put(Context.SECURITY_CREDENTIALS, "secret");
			DirContext context = new InitialDirContext(env);

			return context;
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(e); 
		}	
	}

	public void displayAttributes(Attributes attributes) {

		try {

			System.out.println("Surname: " + attributes.get("sn").get());
			System.out.println("Common name : " + attributes.get("cn").get());
			System.out.println("Telephone number : " + attributes.get("mobile").get());
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(e); 
		}	
	}

	public void add(String empId, String input, String type) {

		ModificationItem[] mods = new ModificationItem[1];
		mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE,
				new BasicAttribute(type, input));
		String value = "employeeNumber=" + empId;

		try {

			getConnection().modifyAttributes(value, mods);
			System.out.println("AfterAdding");
			Attributes attrs1 = getConnection().getAttributes(value);
			displayAttributes(attrs1);
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(e); 
		}

	}

	public void readOne(String empId) { 

		String value = "employeeNumber=" + empId;
		try {

			Attributes attrs = getConnection().getAttributes(value);
			System.out.println(attrs);
			System.out.println("BeforeUpdating");
			displayAttributes(attrs);
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public void update(String empId, String input, String type) {

		ModificationItem[] mods = new ModificationItem[1];

		mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				new BasicAttribute(type, input));
		String value = "employeeNumber=" + empId;

		try {

			getConnection().modifyAttributes(value, mods);
			System.out.println("AfterUpdating");
			Attributes attrs1 = getConnection().getAttributes(value);
			displayAttributes(attrs1);
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	public static void main(String[] args) {

		ldapSampleConnection sample = new ldapSampleConnection();
		sample.readOne("1205");
		sample.update("1205", "anbu", "sn");
		sample.add("1205", "8110008888", "mobile");
	}
}