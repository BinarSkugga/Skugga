package com.binarskugga.skugga.api.impl.parse;

import com.binarskugga.primitiva.reflection.PrimitivaReflection;
import com.binarskugga.skugga.ServerProperties;
import com.binarskugga.skugga.api.Parser;
import com.binarskugga.skugga.api.annotation.IgnoreParser;
import com.binarskugga.skugga.api.annotation.UseParser;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.List;

public abstract class ParsingHandler<P extends Parser, T> {

	private Class<? extends Parser> parserClass;

	public ParsingHandler(Class<? extends Parser> parserClass) {
		this.parserClass = parserClass;
	}

	@SuppressWarnings("unchecked")
	static void init(Class<? extends Parser> parserType, List parsers) {
		Reflections reflections = new Reflections(
				new ConfigurationBuilder().forPackages(
						"com.binarskugga.skugga",
						ServerProperties.getRootPackage()
				)
		);
		for (Class<? extends Parser> parserClass : reflections.getSubTypesOf(parserType)) {
			if (!parserClass.isAnnotationPresent(IgnoreParser.class))
				parsers.add(PrimitivaReflection.constructOrNull(parserClass));
		}
	}

	public abstract List<P> getParsers();

	@SuppressWarnings("unchecked")
	public P getParser(T context, UseParser useParserAnnotation) {
		P defaultParser = null;
		for (P parser : this.getParsers())
			if (parser.predicate(context)) defaultParser = parser;

		P parser = defaultParser;
		if (useParserAnnotation != null) {
			Class clazz = useParserAnnotation.value();
			if (this.parserClass.isAssignableFrom(clazz)) {
				parser = (P) PrimitivaReflection.constructOrNull(clazz);
				if (parser == null) parser = defaultParser;
			}
		}

		if(parser == null) return this.getParsers().get(0);
		return parser;
	}

}
