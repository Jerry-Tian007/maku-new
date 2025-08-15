<script>
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    },
    showIcon: {
      type: Boolean,
      default: true
    },
    showLine: {
      type: Boolean,
      default: false
    },
    isEnd:{
      type: Boolean,
      default: false
    },
    iconColor: {
      type: String,
      default: ''
    }
  },
  render(h, context) {
    const {icon, title, showIcon, iconColor} = context.props
    const vnodes = []

    if (icon && showIcon) {
      if(iconColor){
        vnodes.push(<svg-icon icon-class={icon} color={iconColor}/>)
      }else {
        vnodes.push(<svg-icon icon-class={icon}/>)
      }
    }


    if (title) {
      if (title.length > 5) {
        vnodes.push(<span slot='title' title={(title)}>{(title)}</span>)
      } else {
        vnodes.push(<span slot='title'>{(title)}</span>)
      }
    }
    return vnodes
  }
}
</script>
