package com.simulator.forum.search;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;

public class Search {
	
	
	private static StanfordCoreNLP pipeline;
	
	public static Properties props = new Properties();
	
	
	public static boolean initializeSearchPipeline() 
	{
		props.setProperty("annotators", "tokenize,pos,lemma,ner");
		props.setProperty("ner.useSUTime", "false");
		pipeline = new StanfordCoreNLP(props);
		
		return true;
	}
	
	public void keyphrase(String query)
	{
		CoreDocument doc = new CoreDocument(query);
		pipeline.annotate(doc);
		
		System.out.println("tokens and ner tags");
//	    String tokensAndNERTags = doc.tokens().stream().map(token -> "("+token.word()+","+token.ner()+")").collect(
//	        Collectors.joining(" "));
//	    System.out.println(tokensAndNERTags);
		
//		doc.tokens().
//		Sentence sentence = new Sentence(query);
//		return sentence.nerTags();
		
	}

}
