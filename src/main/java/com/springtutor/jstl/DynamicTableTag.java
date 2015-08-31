package com.springtutor.jstl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

public class DynamicTableTag extends SimpleTagSupport {
	private Object items;
	private String columns;
	private String config;

	@Override
	public void doTag() throws JspException, IOException {
		if (!(items instanceof List)) {
			throw new JspException("'items' property must be a List");
		}
		List<Object> list = (List<Object>) items;
		
		Config configuration = null;
		try {
			config = config.replaceAll("'", "\"");
			ObjectMapper mapper = new ObjectMapper();
			configuration = mapper.readValue(config, Config.class);
		} catch (JsonGenerationException e) {
			//throw new JspException("Error in config attribute", e);
		}
		
				
		List<Property> props = getObjectProperties(list.get(0), columns, configuration); // AKO JE LISTA PRAZNA???

		JspWriter out = getJspContext().getOut();
		
		//out.println("<h3>" + configuration.getColumns().size() + "<h3>");
		
		out.write("<div class=\"oceanDynamicTable\">");
		out.write("<table>");
		out.write("<thead>");
		out.write("<tr>");

		for (Property prop : props) {
			out.write("<th>" + prop.getName() + "</th>");
		}

		out.write("</tr>");
		out.write("</thead>");
		out.write("<tbody>");

		for (Object obj : list) {
			out.write("<tr>");
			for (Property prop : props = getObjectProperties(obj, columns, configuration)) {
				out.write("<td>" + prop.getValue() + "</td>");
			}
			out.write("</tr>");
		}

		out.write("</tbody>");
		out.write("</table>");
		out.write("</div>");

	}

	private List<Property> getObjectProperties(Object o, String columns, Config configuration) throws JspException{
		List<Property> props = new ArrayList<Property>();
		
		// 1. prolaz: upuni vrijednosti svih kolona
		for (Column column : configuration.getColumns()) {
			try {
				Method method = o.getClass().getMethod("get" + column.getId().substring(0,1).toUpperCase() + 
						column.getId().substring(1), null);
				Object propertyValue = method.invoke(o);
				props.add(new Property(column.getId(), column.getTitle(), propertyValue));
			} catch (Exception e) {
				throw new JspException("Error while creating table", e);
			}
		}
		
		// 2. prolaz: potrazi polja koja ovise od drugih polja i upuni ih
		int i = 0;
		for (Column column : configuration.getColumns()) {
			if (column.getType() != null && column.getType().equals("image")) {
				for (Property p : props) {
					if (column.getUrl().contains("{" + p.getId() + "}")) {
						column.setUrl(column.getUrl().replace("{" + p.getId() + "}", p.getValue().toString()));
						props.get(i).setValue(column.getUrl());
					}
				}
			}
			i++;
		}
		
		return props;
	}

	public void setItems(Object items) {
		this.items = items;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}
	
	

	public void setConfig(String config) {
		this.config = config;
	}



	private class Property {
		private String id;
		private String name;
		private Object value;

		public Property(String id, String name, Object value) {
			this.id = id;
			this.name = name;
			this.value = value;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}



		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

	}
	
	private static class Config {
		private List<Column> columns = new ArrayList<DynamicTableTag.Column>();
		
		public Config() {}
		
		public List<Column> getColumns() {
			return columns;
		}
		public void setColumns(List<Column> columns) {
			this.columns = columns;
		}
	}
	
	// One column of table
	private static class Column {
		private String id;
		private String title;
		private String type;
		private String url;
		public Column() {}
		public Column(String id, String title, String type, String url) {
			this.id = id;
			this.title = title;
			this.type = type;
			this.url = url;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}

}
