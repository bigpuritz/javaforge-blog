import japa.parser.*
import japa.parser.ast.*
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;

def engine = new groovy.text.SimpleTemplateEngine()
def utilClassTmpl = engine.createTemplate('''

	/**
	 * Utility class to get the instance of the Rest Service
	 */
	public static final class Util {

		private static ${asyncResourceClass} instance;

		public static final ${asyncResourceClass} get() {
			if (instance == null) {
				instance = com.google.gwt.core.client.GWT.create(${asyncResourceClass}.class);
				((org.fusesource.restygwt.client.RestServiceProxy) instance).setResource(
						new org.fusesource.restygwt.client.Resource(${asyncResourcePath})
				);
			}
			return instance;
		}

		private Util() {
			// Utility class should not be instantiated
		}
	}

''')

def methodTmpl = engine.createTemplate('''
	${methodAnnotations}
	public void ${methodName}(${methodParams}${methodCallback});\n
''')

def restAsyncResourcesPath = project.properties['restAsyncResourcesPath']
def restAsyncResourcesPackage = project.properties['restAsyncResourcesPackage']
def restResourcesPath = project.properties['restResourcesPath']
def restURLBase = project.properties['restURLBase']
def importsRegexp = ~project.properties['importsRegexp']

new File(restResourcesPath).eachFileMatch(~/.*?\.java/) { file ->

    println "Reading: ${file.absolutePath}"

    FileInputStream _in = new FileInputStream(file)
    CompilationUnit cu;
    try {

        cu = JavaParser.parse(_in)
        TypeDeclaration resource = cu.types.first();

        def pathAnnotation = resource.annotations.find { it instanceof japa.parser.ast.expr.SingleMemberAnnotationExpr && it.name.toString().equals("Path") }
        def asyncResourcePath = "\"${restURLBase}${pathAnnotation.memberValue.toString()[1..-1]}"
        def asyncResourceClass = "${resource.name}Async"

        println "Writing: ${asyncResourceClass}.java"

        File baseDir = new File(restAsyncResourcesPath);
        baseDir.mkdirs();

        File target = new File(baseDir, "${asyncResourceClass}.java")
        if (target.exists()) target.delete();

        target << ("package ${restAsyncResourcesPackage};\n\n")

        cu.imports.each() {
            if (importsRegexp.matcher(it.name.toString()).matches())
                target << ("$it")
        }

        target << ("\n")
        target << ("public interface $asyncResourceClass extends org.fusesource.restygwt.client.RestService {\n")

        resource.members.each() { member ->
            if (member instanceof MethodDeclaration && ModifierSet.isPublic(member.modifiers)) {
                def mAnnotations = ""
                member.annotations.each {  ann -> mAnnotations += "${ann} " }
                def methodParams = ""
                member.parameters.each { param ->  methodParams += "${param}, " }
                def methodCallback = "org.fusesource.restygwt.client.MethodCallback<" + (member.type instanceof japa.parser.ast.type.VoidType ? "Void" : member.type.toString()) + "> callback"
                target << (methodTmpl.make(["methodAnnotations": mAnnotations, "methodName": member.name.toString(), "methodParams": methodParams, "methodCallback": methodCallback]).toString())
            }
        }

        target << (utilClassTmpl.make(["asyncResourceClass": asyncResourceClass, "asyncResourcePath": asyncResourcePath]).toString())
        target << ("}")

    } catch (Throwable t) {

        t.printStackTrace()

    } finally {

        _in.close();
    }
}