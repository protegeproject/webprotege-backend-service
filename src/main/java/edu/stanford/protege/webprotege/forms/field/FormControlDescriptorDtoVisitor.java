package edu.stanford.protege.webprotege.forms.field;

public interface FormControlDescriptorDtoVisitor<R> {

    R visit(TextControlDescriptorDto textControlDescriptorDto);

    R visit(SingleChoiceControlDescriptorDto singleChoiceControlDescriptorDto);

    R visit(MultiChoiceControlDescriptorDto multiChoiceControlDescriptorDto);

    R visit(NumberControlDescriptorDto numberControlDescriptorDto);

    R visit(ImageControlDescriptorDto imageControlDescriptorDto);

    R visit(GridControlDescriptorDto gridControlDescriptorDto);

    R visit(SubFormControlDescriptorDto subFormControlDescriptorDto);

    R visit(EntityNameControlDescriptorDto entityNameControlDescriptorDto);
}
