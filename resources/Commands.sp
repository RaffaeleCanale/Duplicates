duplicates:
	help = TODO
	properties:
		files:
			help = Directories to analyze
			count = 1 -1
		indexers:
			help = TODO
			count = 1 -1
			markers = --comp
			default = size name
		min_duplicates:
			help = TODO
			count = 1 1
			markers = --min-dup
			type = integer
			default = 2
		min_size:
			help = TODO
			count = 1 1
			markers = --min-size
			type = integer
			default = 1
		max_size:
			help = TODO
			markers = --max-size
			type = integer
			default = 0
		checksum_max_depth:
			help = TODO
			count = 1 1
			markers = --cs-max-depth
			type = integer
			default = -1
		checksum_size:
			help = TODO
			count = 1 1
			markers = --cs-size
			type = integer
			default = $2
		checksum_step:
			help = TODO
			count = 1 1
			markers = --cs-step
			type = integer
			default = $3

