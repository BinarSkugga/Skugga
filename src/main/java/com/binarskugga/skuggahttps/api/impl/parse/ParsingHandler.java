package com.binarskugga.skuggahttps.api.impl.parse;

import com.binarskugga.skuggahttps.api.Parser;
import com.binarskugga.skuggahttps.api.annotation.UseParser;
import com.binarskugga.skuggahttps.util.ReflectionUtils;
import org.reflections.Reflections;

import java.util.List;

public abstract class ParsingHandler<P extends Parser, T> {

	private Class<? extends Parser> parserClass;

	public ParsingHandler(Class<? extends Parser> parserClass) {
		this.parserClass = parserClass;
	}

	@SuppressWarnings("unchecked")
	static void init(String parserPackage, Class<? extends Parser> parserType, List parsers) {
		Reflections reflections = new Reflections(parserPackage);
		for(Class<? extends Parser> parserClass : reflections.getSubTypesOf(parserType))
			parsers.add(ReflectionUtils.constructOrNull(parserClass));
	}

	public abstract List<P> getParsers();

	@SuppressWarnings("unchecked")
	public P getParser(T context, UseParser useParserAnnotation) {
		P defaultParser = null;
		for(P parser : this.getParsers())
			if(parser.predicate(context)) defaultParser = parser;

		P parser = defaultParser;
		if(useParserAnnotation != null) {
			Class clazz = useParserAnnotation.value();
			if(this.parserClass.isAssignableFrom(clazz)) {
				parser = (P) ReflectionUtils.constructOrNull(clazz);
				if (parser == null) parser = defaultParser;
			}
		}
		return parser;
	}

}
