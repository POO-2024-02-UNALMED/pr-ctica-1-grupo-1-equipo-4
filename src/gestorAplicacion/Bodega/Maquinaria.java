
							}
						}
					}
				} else{
					cadaMaquina.mantenimiento = true;
					// ver como hacer para realizar la revision y que la maquina pueda volver a ser utilizada
					cadaMaquina.ultFechaRevision = fecha;
				}
				
				//añadir esta maquina a la lista temporal de las maquinas que estan disponibles
				if(cadaMaquina.mantenimiento == false && cadaMaquina.estado == true){
					maqDisponibles.add(cadaMaquina);
				}
			}
		}

		return maqDisponibles;
	}

	public ArrayList<Proveedor> encontrarProveedoresBaratos(){

		for(Repuesto cadaRepuesto : Repuesto.getListadoRepuestoss()){
			proveedorBarato = null;
			for(Proveedor proveedores : Proveedor.getListaProveedores()){
				//ver si el nombre del repuesto o insumo es igual al nombre del insumo que tiene el proveedor en su atributo insumo
				//o sea, que vende el proveedor:
				if(proveedores.getInsumo().getNombre().equalsIgnoreCase(cadaRepuesto.getNombre())){
					//con estos if se determina cual proveedor vende mas barato el insumo a reemplazar
					//y se guarda en la variable proveedorBarato:
					if(proveedorBarato == null){
						proveedorBarato = proveedores;
					}
					else if(proveedores.getInsumo().getPrecioIndividual() <= proveedorBarato.getInsumo().getPrecioIndividual()){
						proveedorBarato = proveedores;
					}

				}
			}

			listProveedoresBaratos.add(proveedorBarato);
		}	
		proveedorBarato = null;

		return listProveedoresBaratos;
	}

	static public void asignarMaquinaria(Empleado emp){
		
		ArrayList<String> maquinariaPorAsignar = new ArrayList<>(emp.getAreaActual().getMaquinariaNecesaria());
		for (Maquinaria maq : emp.getSede().getlistaMaquinas()){
			if(maquinariaPorAsignar.contains(maq.nombre) || maq.user==null){
				maq.user = emp;
				maquinariaPorAsignar.remove(maq.nombre);
				break;
			}
		}
	}
}