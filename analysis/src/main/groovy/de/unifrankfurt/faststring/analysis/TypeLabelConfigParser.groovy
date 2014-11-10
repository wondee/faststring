package de.unifrankfurt.faststring.analysis;

import com.ibm.wala.shrikeBT.Util;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

import de.unifrankfurt.faststring.analysis.label.TypeLabel;
import de.unifrankfurt.faststring.analysis.label.TypeLabelConfig
import groovy.json.JsonSlurper;

/**
 * parser for {@code .type} files. Creates a {@link TypeLabelConfig}
 *
 * @author markus
 *
 */
class TypeLabelConfigParser {

	def typeRef

	TypeLabel parseFile(String path) {

		def reader = createReader(path)


		def json = new JsonSlurper().parse(reader)

		def config = new TypeLabelConfig()

		config.name = json.name
		config.originalType = Class.forName(json.originalType)
		config.optimizedType = Class.forName(json.optimizedType)

		config.staticFactory = json.staticFactory
		config.toOrignalMethodName = json.toOriginalMethod

		def typeName = Util.makeType(config.originalType)

		typeRef = TypeReference.findOrCreate(ClassLoaderReference.Application, typeName.substring(0, typeName.length() - 1));

		def methodRefs = createMethodRefs(json.methodsDefs)

		def getMethods = { it.collect { methodRefs[it] } }

		config.effectedMethods = getMethods(json.effectedMethods)
		config.supportedMethods = getMethods(json.supportedMethods)
		config.producingMethods = getMethods(json.producingMethods)

		config.paramDifference = collectMap(json.optimizedParams, { m, p -> [(methodRefs[m]) : p] })

		config.paramUsage = collectMap(json.parameterUsage, { m, is -> [(methodRefs[m]) : is as Set] })

		config.compatibleLabels = json.compatibleLabels

		return config
	}

	def collectMap(map, f) {
		def m = map ?: [:]
		m.collectEntries (f)
	}


	def createMethodRefs(list) {
		list.collectEntries {
			[(it.id) : MethodReference.findOrCreate(typeRef, it.desc.name, it.desc.signature)]
		}
	}

	def createReader(path) {
		def file = new File(path)
		if (!file.exists()) {
			def is = getClass().getResourceAsStream("/" + path)

			if (is == null) {
				throw new FileNotFoundException(path, "file could not be found as resource");
			}

			new InputStreamReader(is)
		} else {
			new FileReader(file)
		}
	}

}
