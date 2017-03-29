package ua.com.yaremko.system.core;

import java.util.Collections;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerUtilities;
import org.protege.editor.owl.ui.clsdescriptioneditor.ExpressionEditor;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Process current dlQuery reasoner
 * 
 * @author Solomka
 *
 */
public class DLQuery {

	private static final Logger LOGGER = LoggerFactory.getLogger(DLQuery.class);

	private final OWLEditorKit owlEditorKit;
	private ExpressionEditor<OWLClassExpression> owlExpressionEditor;
	private OWLReasonerManager reasonerManager;
	private OWLClassExpression owlClassExpression;
	private ShortFormProvider shortFormProvider;
	private DLQueryPrinter dlQueryPrinter;

	public DLQuery(OWLEditorKit owlEditorKit) {
		this.owlEditorKit = owlEditorKit;
		/*
		 * final OWLExpressionChecker<OWLClassExpression> checker =
		 * owlEditorKit.getOWLModelManager()
		 * .getOWLExpressionCheckerFactory().getOWLClassExpressionChecker();
		 * owlExpressionEditor = new ExpressionEditor<>(owlEditorKit, checker);
		 * shortFormProvider = new SimpleShortFormProvider();
		 */

		reasonerManager = owlEditorKit.getOWLModelManager().getOWLReasonerManager();
		ReasonerUtilities.warnUserIfReasonerIsNotConfigured(owlEditorKit.getOWLWorkspace(), reasonerManager);

		OWLReasoner reasoner = reasonerManager.getCurrentReasoner();
		shortFormProvider = new SimpleShortFormProvider();
		// Create the DLQueryPrinter helper class. This will manage the
		// parsing of input and printing of results
		dlQueryPrinter = new DLQueryPrinter(new DLQueryEngine(reasoner, shortFormProvider), shortFormProvider);
	}
	
	public Set<OWLClass> getSubClasses(String classExpression, boolean direct){
		return dlQueryPrinter.printSubClasses(classExpression, direct);
	}
		

	/*
	 * process OWLReasoner request
	 */
	
	class DLQueryEngine {

		private final OWLReasoner reasoner;
		private final DLQueryParser parser;

		/**
		 * Constructs a DLQueryEngine. This will answer "DL queries" using the
		 * specified reasoner. A short form provider specifies how entities are
		 * rendered.
		 * 
		 * @param reasoner
		 *            The reasoner to be used for answering the queries.
		 * @param shortFormProvider
		 *            A short form provider.
		 */
		public DLQueryEngine(OWLReasoner reasoner, ShortFormProvider shortFormProvider) {
			this.reasoner = reasoner;
			OWLOntology rootOntology = reasoner.getRootOntology();
			parser = new DLQueryParser(rootOntology, shortFormProvider);
		}

		/**
		 * Gets the superclasses of a class expression parsed from a string.
		 * 
		 * @param classExpressionString
		 *            The string from which the class expression will be parsed.
		 * @param direct
		 *            Specifies whether direct superclasses should be returned
		 *            or not.
		 * @return The superclasses of the specified class expression If there
		 *         was a problem parsing the class expression.
		 */
		public Set<OWLClass> getSuperClasses(String classExpressionString, boolean direct) {
			if (classExpressionString.trim().length() == 0) {
				return Collections.emptySet();
			}
			OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
			NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(classExpression, direct);
			return superClasses.getFlattened();
		}

		/**
		 * Gets the equivalent classes of a class expression parsed from a
		 * string.
		 * 
		 * @param classExpressionString
		 *            The string from which the class expression will be parsed.
		 * @return The equivalent classes of the specified class expression If
		 *         there was a problem parsing the class expression.
		 */
		public Set<OWLClass> getEquivalentClasses(String classExpressionString) {
			if (classExpressionString.trim().length() == 0) {
				return Collections.emptySet();
			}
			OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
			Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
			Set<OWLClass> result;
			if (classExpression.isAnonymous()) {
				result = equivalentClasses.getEntities();
			} else {
				result = equivalentClasses.getEntitiesMinus(classExpression.asOWLClass());
			}
			return result;
		}

		/**
		 * Gets the subclasses of a class expression parsed from a string.
		 * 
		 * @param classExpressionString
		 *            The string from which the class expression will be parsed.
		 * @param direct
		 *            Specifies whether direct subclasses should be returned or
		 *            not.
		 * @return The subclasses of the specified class expression If there was
		 *         a problem parsing the class expression.
		 */
		public Set<OWLClass> getSubClasses(String classExpressionString, boolean direct) {
			if (classExpressionString.trim().length() == 0) {
				return Collections.emptySet();
			}
			OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
			System.out.println("classExpression" + classExpression.toString());
			NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
			return subClasses.getFlattened();
		}

		/**
		 * Gets the instances of a class expression parsed from a string.
		 * 
		 * @param classExpressionString
		 *            The string from which the class expression will be parsed.
		 * @param direct
		 *            Specifies whether direct instances should be returned or
		 *            not.
		 * @return The instances of the specified class expression If there was
		 *         a problem parsing the class expression.
		 */
		public Set<OWLNamedIndividual> getInstances(String classExpressionString, boolean direct) {
			if (classExpressionString.trim().length() == 0) {
				return Collections.emptySet();
			}
			OWLClassExpression classExpression = parser.parseClassExpression(classExpressionString);
			NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression, direct);
			return individuals.getFlattened();
		}
	}

	/*
	 * parse form String to OWLClassExpression
	 */
	class DLQueryParser {

		private final OWLOntology rootOntology;
		private final BidirectionalShortFormProvider bidiShortFormProvider;

		/**
		 * Constructs a DLQueryParser using the specified ontology and short
		 * form provider to map entity IRIs to short names.
		 * 
		 * @param rootOntology
		 *            The root ontology. This essentially provides the domain
		 *            vocabulary for the query.
		 * @param shortFormProvider
		 *            A short form provider to be used for mapping back and
		 *            forth between entities and their short names (renderings).
		 */
		public DLQueryParser(OWLOntology rootOntology, ShortFormProvider shortFormProvider) {
			this.rootOntology = rootOntology;
			OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
			Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
			// Create a bidirectional short form provider to do the actual
			// mapping.
			// It will generate names using the input
			// short form provider.
			bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager, importsClosure,
					shortFormProvider);
		}

		/**
		 * Parses a class expression string to obtain a class expression.
		 * 
		 * @param classExpressionString
		 *            The class expression string
		 * @return The corresponding class expression if the class expression
		 *         string is malformed or contains unknown entity names.
		 */
		public OWLClassExpression parseClassExpression(String classExpressionString) {
			OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager().getOWLDataFactory();

			// Set up the real parser
			ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(dataFactory,
					classExpressionString);
			parser.setDefaultOntology(rootOntology);
			// Specify an entity checker that wil be used to check a class
			// expression contains the correct names.
			OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
			parser.setOWLEntityChecker(entityChecker);
			// Do the actual parsing

			OWLClassExpression expr = parser.parseClassExpression();

			System.out.println("Expr: " + expr.toString());

			return expr;
		}
	}

	/*
	 * print DLQueryEngine result
	 */
	class DLQueryPrinter {

		private final DLQueryEngine dlQueryEngine;
		private final ShortFormProvider shortFormProvider;

		/**
		 * @param engine
		 *            the engine
		 * @param shortFormProvider
		 *            the short form provider
		 */
		public DLQueryPrinter(DLQueryEngine engine, ShortFormProvider shortFormProvider) {
			this.shortFormProvider = shortFormProvider;
			dlQueryEngine = engine;
		}

		/**
		 * @param classExpression
		 *            the class expression to use for interrogation
		 */
		
		public Set<OWLClass> printSuperClass(String classExpression , boolean direct)
		{
			Set<OWLClass> superClasses = dlQueryEngine.getSuperClasses(classExpression, direct);
			
			return superClasses;
		}
		
		public Set<OWLClass> printEquivalentClasses(String classExpression)
		{
			Set<OWLClass> equivalentClasses = dlQueryEngine.getEquivalentClasses(classExpression);
			return equivalentClasses;
			
		}
		public Set<OWLClass> printSubClasses(String classExpression, boolean direct)
		{
			Set<OWLClass> subClasses = dlQueryEngine.getSubClasses(classExpression, direct);
			return subClasses;
		}
		public Set<OWLNamedIndividual> printInstances(String classExpression, boolean direct)
		{
			Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(classExpression, direct);
			return individuals;
			
		}
		/*
		public void askQuery(String classExpression) {
			if (classExpression.length() == 0) {
				System.out.println("No class expression specified");
			} else {
				try {
					StringBuilder sb = new StringBuilder();
					sb.append("\n--------------------------------------------------------------------------------\n");
					sb.append("QUERY:   ");
					sb.append(classExpression);
					sb.append("\n");
					sb.append("--------------------------------------------------------------------------------\n\n");
					// Ask for the subclasses, superclasses etc. of the
					// specified
					// class expression. Print out the results.
					Set<OWLClass> superClasses = dlQueryEngine.getSuperClasses(classExpression, true);
					printEntities("SuperClasses", superClasses, sb);
					Set<OWLClass> equivalentClasses = dlQueryEngine.getEquivalentClasses(classExpression);
					printEntities("EquivalentClasses", equivalentClasses, sb);
					Set<OWLClass> subClasses = dlQueryEngine.getSubClasses(classExpression, false);
					printEntities("SubClasses", subClasses, sb);
					Set<OWLNamedIndividual> individuals = dlQueryEngine.getInstances(classExpression, true);
					printEntities("Instances", individuals, sb);
					System.out.println(sb.toString());
				} catch (ParserException e) {
					System.out.println(e.getMessage());
				}
			}
		}

		private void printEntities(String name, Set<? extends OWLEntity> entities, StringBuilder sb) {
			sb.append(name);
			int length = 50 - name.length();
			for (int i = 0; i < length; i++) {
				sb.append(".");
			}
			sb.append("\n\n");
			if (!entities.isEmpty()) {
				for (OWLEntity entity : entities) {
					sb.append("\t");
					sb.append(shortFormProvider.getShortForm(entity));
					sb.append("\n");
				}
			} else {
				sb.append("\t[NONE]\n");
			}
			sb.append("\n");
		}
		*/

	}
}
