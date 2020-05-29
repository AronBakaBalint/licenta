package aron.utcn.licenta.converter;

public interface BaseConverter<Entity, Dto> {

	public Entity convertToEntity(Dto dto);
	
	public Dto convertToDto(Entity entity);
}
