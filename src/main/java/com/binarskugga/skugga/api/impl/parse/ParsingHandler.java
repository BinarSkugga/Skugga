package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.primitiva.Primitiva;
import com.binarskugga.primitiva.reflection.TypeReflector;
import com.binarskugga.skugga.ServerProperties;
import com.binarskugga.skugga.api.Parser;
import com.binarskugga.skugga.api.annotation.IgnoreParser;
import com.binarskugga.skugga.api.annotation.UseParser;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.LinkedList;
import java.util.List;

public abstract class ParsingHandler<P extends Parser, T> {

	private Class<? extends Parser> parserClass;

	public ParsingHandler(Class<? extends Parser> parserClass) {
		this.parserClass = parserClass;
		this.init(parserClass);
	}

	@SuppressWarnings("unchecked")
	private void init(Class<? extends Parser> parserType) {
		List<P> parsers = new LinkedList<>();
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().forPackages(
						ServerProperties.getRootPackage(),
						ServerProperties.getSkuggaPackage()
				)
		);

		List<P> defaultParsers = new LinkedList<>();
		for (Class<? extends Parser> parserClass : reflections.getSubTypesOf(parserType)) {
			TypeReflector parserReflector = Primitiva.Reflection.ofType(parserClass);
			if(parserClass.getName().startsWith(ServerProperties.getSkuggaPackage()))
				defaultParsers.add((P) parserReflector.create());
			else if (!parserClass.isAnnotationPresent(IgnoreParser.class))
				parsers.add((P) parserReflector.create());
		}
		parsers.addAll(defaultParsers);

		this.setParsers(parsers);
	}

	public abstract List<P> getParsers();
	public abstract void setParsers(List<P> parsers);

	@SuppressWarnings("unchecked")
	public P getParser(T context, UseParser useParserAnnotation) {
		P defaultParser = null;
		for (P parser : this.getParsers()) {
			if (defaultParser != null) continue;
			if (parser.predicate(context)) defaultParser = parser;
		}

		P parser = defaultParser;
		if (useParserAnnotation != null) {
			Class clazz = useParserAnnotation.value();
			if (this.parserClass.isAssignableFrom(clazz)) {
				parser = (P) Primitiva.Reflection.ofType(clazz).create();
				if (parser == null) parser = defaultParser;
			}
		}

		return parser;
	}

}
