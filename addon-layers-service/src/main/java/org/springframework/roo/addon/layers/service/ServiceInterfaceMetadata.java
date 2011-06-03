package org.springframework.roo.addon.layers.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.classpath.scanner.MemberDetails;
import org.springframework.roo.project.layers.CrudKey;
import org.springframework.roo.project.layers.MemberTypeAdditions;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.Path;
import org.springframework.roo.support.style.ToStringCreator;
import org.springframework.uaa.client.util.Assert;

/**
 * 
 * @author Stefan Schmidt
 * @since 1.2
 */
public class ServiceInterfaceMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
	private static final String PROVIDES_TYPE_STRING = ServiceInterfaceMetadata.class.getName();
	private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
	private ServiceAnnotationValues annotationValues;
	private MemberDetails governorDetails;
	
	public ServiceInterfaceMetadata(String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, MemberDetails governorDetails, ServiceAnnotationValues annotationValues, Map<JavaType, Map<CrudKey, MemberTypeAdditions>> allCrudAdditions) {
		super(identifier, aspectName, governorPhysicalTypeMetadata);
		Assert.notNull(governorDetails, "Governor member details required");
		Assert.notNull(annotationValues, "Annotation values required");
		Assert.notNull(governorDetails, "Governor member details required");
		
		this.annotationValues = annotationValues;
		this.governorDetails = governorDetails;
		
		for (JavaType domainType : annotationValues.getDomainTypes()) {
			Map<CrudKey, MemberTypeAdditions> crudAdditions = allCrudAdditions.get(domainType);
			
			MemberTypeAdditions findAllAdditions = crudAdditions.get(CrudKey.FIND_ALL_METHOD);
			if (findAllAdditions != null) {
				builder.addMethod(getFindAllMethod(domainType, findAllAdditions));
			}
		}
		
		// Create a representation of the desired output ITD
		itdTypeDetails = builder.build();
	}
	
	private MethodMetadata getFindAllMethod(JavaType domainType, MemberTypeAdditions findAllAdditions) {
		JavaSymbolName methodName = new JavaSymbolName(annotationValues.getFindAllMethod());
		if (MemberFindingUtils.getMethod(governorDetails, methodName, new ArrayList<JavaType>()) != null) {
			return null;
		}
		InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
		bodyBuilder.appendFormalLine("test");
		MethodMetadataBuilder builder = new MethodMetadataBuilder(getId(), 0, methodName, new JavaType("java.util.List", 0, DataType.TYPE, null, Arrays.asList(domainType)), bodyBuilder);
		return builder.build();
	}

	public static final String getMetadataIdentiferType() {
		return PROVIDES_TYPE;
	}

	public static final String createIdentifier(JavaType javaType, Path path) {
		return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
	}

	public static final JavaType getJavaType(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}

	public static final Path getPath(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}

	public static boolean isValid(String metadataIdentificationString) {
		return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING, metadataIdentificationString);
	}
	
	public String toString() {
		ToStringCreator tsc = new ToStringCreator(this);
		tsc.append("identifier", getId());
		tsc.append("valid", valid);
		tsc.append("aspectName", aspectName);
		tsc.append("destinationType", destination);
		tsc.append("governor", governorPhysicalTypeMetadata.getId());
		tsc.append("itdTypeDetails", itdTypeDetails);
		return tsc.toString();
	}
}
